package dao;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import connection.ConnectionFactory;

public abstract class AbstractDAO<T> {

    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());
    protected final Class<T> type;

    @SuppressWarnings("unchecked")
	protected AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
    
    /**
     * Functia creaza unul sau mai multe obiecte pe baza datelor funizate ca parametru
     * In functie de ce clasa apeleaza aceasta metoda, vor fi create obiecte de aceeasi clasa 
     * @param resultSet - un set de inregistrari, unul sau mai multe, folosite pentru creare obiectelor
     * @return List de clase T - o lista de obiecte de aceasi clasa ca obiectul ce apeleaza metoda
     */
    @SuppressWarnings("deprecation")
	private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        try {
            while (resultSet.next()) {
            	T instance=type.newInstance();

                for (Field field : type.getDeclaredFields()) {
                    Object value = resultSet.getObject(field.getName());
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
                if(list.size() == 0) {
                    throw new Exception("Size 0");
                }
            }
        } catch (InstantiationException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:Create objects " + e.getMessage());
        } catch (IllegalAccessException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:Create objects " + e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:Create objects " + e.getMessage());
        } catch (InvocationTargetException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:Create objects " + e.getMessage());
        } catch (SecurityException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:Create objects " + e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:Create objects " + e.getMessage());
        } catch (IntrospectionException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:Create objects " + e.getMessage());
        } catch (Exception e) {
            if(e.getMessage().compareTo("Size 0") == 0) {
                System.out.println("error - size 0");
            }
        }

        return list;
    }

    /**
     * Functia creaza un string pentru instructiunea de selectie a tuturor
     * campurilor dintr-o tabela a carui nume este generat
     * @return string - un string ce contine instructiunea ce va fi executata asupra bazei de date
     */
    public String createSelectQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT");
        sb.append(" * ");
        sb.append("FROM ");
        sb.append(type.getSimpleName());
        
        return sb.toString();
    }
    
    /**
     * Se creaza un string pentru instructiunea de selectie dupa un anumit camp
     * @param field - numele campului dupa care se face selectia inregistrarii
     * @return string - un string ce reprezinta instructiunea ce va fi executata
     */
    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT");
        sb.append(" * ");
        sb.append("FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field + " = ?");
       
        return sb.toString();
    }
    
    /**
     * Se creaza instructiunea de stergere din baza de date
     * @param id - idul inregistrarii care se va sterge
     * @return string - instructiunea de stergere din baza de date cu idul specificat
     */
    private String createDeleteQuery(int id) {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM " + type.getSimpleName() + " WHERE " + type.getDeclaredFields()[0].getName() + "=" + id);
        return sb.toString();
    }
    
    /**
     * Functia creaza instructiunea de inserarea in tabel, fara a introduce si datele
     * @param t - un obiect de aceeasi clasa ca si obiectul ce apeleaza metoda
     * @return string - un string ce reprezinta instructiunea de inserare 
     */
    private String createInsertQuery(T t) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO `");
        sb.append(type.getSimpleName() + "` (");
        boolean first = true;
        boolean second = true;
        for (Field field : type.getDeclaredFields()) {
            if (first) {
                first = false;
            } else if(second) {
                sb.append(field.getName());
                second = false;
            } else {
                sb.append(", " + field.getName());
            }
        }
        sb.append(") VALUES (");

        try {
            first = true;
            second = true;
            for (Field field : type.getDeclaredFields()) {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                Method method = propertyDescriptor.getReadMethod();
                Object value = method.invoke(t);

                if (first) {
                    first = false;
                } else if(second) {
                    sb.append("'" + value.toString() + "'");
                    second = false;
                } else {
                    sb.append(", '" + value.toString() + "'");
                }
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        sb.append(")");

        return sb.toString();
    }
    
    /**
     * Functia creaza instructiunea de actualizare a datelor din baza de date
     * @param t - obiectul cu datele actualizate
     * @param id - idul la care se face inserarea in table
     * @return string - instructiunea creata ce urmeaza executata
     */
    private String createUpdateQuery(T t, int id) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE " + type.getSimpleName() + " SET ");

        try {
            String idc = "";
            boolean first = true;
            for (Field field : type.getDeclaredFields()) {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                Method method = propertyDescriptor.getReadMethod();
                Object value = method.invoke(t);

                if (first) {
                    sb.append(field.getName() + "='" + value.toString() + "'");
                    idc = field.getName() + "='" + id + "'";
                    first = false;
                } else {
                    sb.append(", " + field.getName() + "='" + value.toString() + "'");
                }
            }

            sb.append(" WHERE " + idc);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    /**
     * Functia cauta toate inregistrarile din o tabela
     * Executa functie de cautare si creaza obiecte stocate in lista
     * @return ArrayList de clasa T - clasa de instanta corespunzatoare clasei ce apeleaza functia
     */
    public ArrayList<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            return (ArrayList<T>) createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return null;
    }

    /**
     * Functia executa cautarea inregistrarii din tabela dupa un id
     * Dupa cautarea inregistrarii se creaza un obiect al carei clase este acelasi cel ce apeleaza clasa
     * @param id - idul dupa care se face cautarea in tabela in baza de date
     * @return Obiect de clasa T - o instanta a clasei ce apeleaza metoda
     */
    public T findByID(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery(type.getDeclaredFields()[0].getName());
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            return createObjects(resultSet).get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById" + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Functia executa cautarea in baza de date a inregistrarilor ce contin un anumit nume
     * Inregistrarile gasite sunt returnate si se creaza obiecte corespunzatoare
     * @param name - numele dupa care se cauta inregistrari
     * @return Obiect de clasa T - o instanta a clasei ce apeleaza metoda
     */
    public T findByName(String name) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery(type.getDeclaredFields()[1].getName());
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, name);
            resultSet = statement.executeQuery();

  
            List<T> l = createObjects(resultSet);
            if( l.size() == 0 )
            	return null;
            else
            	return l.get(0);
            
            
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById" + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Functia apeleaza metoda de creare a instructiunii corespunzatoare
     * Instructiunea este executata pentru inserarea inregistrarii in baza de date
     * @param t - obiectul ce contine datele ce trebuie inserate in baza de date
     * @return id - se returneaza idul la care au fost inserate datele sau -1 in caz de eroare
     */
    public int insert(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createInsertQuery(t);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if(resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return -1;
    }

    /**
     * Se realizeaza crearea instructiunii de update si se actualizeaza inregistrare de la un anumit id cu noul obiect
     * @param t - obiect cu datele actualizate
     * @param id - idul la care trebuie actualizate datele
     * @return boolean - true in caz de succes si false in caz contrar
     */
    public boolean update(T t, int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createUpdateQuery(t, id);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:update " + e.getMessage());
            return false;
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return true;
    }

    /**
     * Se executa stergerea din baza de date a inregistrarii cu idul specificat
     * @param id - idul inregistrarii ce trebuie sterse
     * @return boolean - true in caz de succes, false in caz contrar
     */
    public boolean delete(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createDeleteQuery(id);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:delete " + e.getMessage());
            return false;
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return true;
    }
}
