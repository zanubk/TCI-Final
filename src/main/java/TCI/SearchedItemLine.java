package TCI;

import java.util.concurrent.TimeUnit;

public class SearchedItemLine<T>
{
    private  T founditem;
    private String time_elapse;
    private long id;
    public String  getTime_elapse() {
        return time_elapse;
    }

    public void setTime_elapse(long start_time_elapse, long end_time_elapse) {
        long diff = end_time_elapse-start_time_elapse;
        long sec = TimeUnit.NANOSECONDS.toMillis(diff);
        this.time_elapse= diff + " ns";

    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public T getFounditem() {
        return founditem;
    }

    public void setFounditem(T founditem) {
        this.founditem = founditem;
    }
    public  SearchedItemLine(T item)
    {
        this.founditem = item;
    }
}
