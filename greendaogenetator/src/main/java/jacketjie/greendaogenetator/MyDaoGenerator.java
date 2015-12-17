package jacketjie.greendaogenetator;


import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Created by Administrator on 2015/11/12.
 */
public class MyDaoGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "jacketjie.astimes.greenDao");

//        addAdsNote(schema);
        addNote(schema);
        addAdsNote(schema);
//        addCustomerOrder(schema);

        new DaoGenerator().generateAll(schema, "../AsTimes/app/src/main/java");
    }
    //用户
    private static void addNote(Schema schema) {
        Entity note = schema.addEntity("ATUser");
        note.addIdProperty();
        note.addIntProperty("userId").notNull().unique();
        note.addStringProperty("userName").notNull();
        note.addStringProperty("userPassword");
        note.addStringProperty("userPhonenumber");
        note.addStringProperty("userNickName").notNull().unique();
        note.addStringProperty("userIcon");
        note.addStringProperty("userSignature");
        note.addIntProperty("userGender");
        note.addBooleanProperty("isActiveUser");
        note.addDateProperty("updateTime");
    }
    //随笔
    private static void addAdsNote(Schema schema) {
        Entity note = schema.addEntity("ATInformalEssay");
        note.addIdProperty();
        note.addStringProperty("ATIEId").notNull().unique();
        note.addStringProperty("ATIEImageUrl");
        note.addStringProperty("ATIETitle");
        note.addStringProperty("ATIEText");
        note.addStringProperty("ATIEReleaseDate");
        note.addIntProperty("ATIEHasSubmit").notNull();
        note.addIntProperty("ATIEShared").notNull();
    }

    private static void addCustomerOrder(Schema schema) {
        Entity customer = schema.addEntity("Customer");
        customer.addIdProperty();
        customer.addStringProperty("name").notNull();

        Entity order = schema.addEntity("Order");
        order.setTableName("ORDERS"); // "ORDER" is a reserved keyword
        order.addIdProperty();
        Property orderDate = order.addDateProperty("date").getProperty();
        Property customerId = order.addLongProperty("customerId").notNull().getProperty();
        order.addToOne(customer, customerId);

        ToMany customerToOrders = customer.addToMany(order, customerId);
        customerToOrders.setName("orders");
        customerToOrders.orderAsc(orderDate);
    }

}
