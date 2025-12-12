package bt.models;

import java.io.Serializable;


public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    // تعريف الـ ID هنا كخاصية محمية (Protected) ليتم استخدامه في الكلاسات الأبناء
    protected int id;


    public abstract String getDisplayInfo();


    public int getId() {
        return id;
    }
}