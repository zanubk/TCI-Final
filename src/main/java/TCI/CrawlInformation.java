package TCI;

import java.util.concurrent.TimeUnit;

public class CrawlInformation {
    public int Explorer;
    public int Depth;
    private String time_elapse;
    public int GetEXplorer()
    {
        return Explorer;
    }
    public void SetExplorer(int e)
    {Explorer = e;}
    public int GetDepth()
    {
        return Depth;
    }
    public void SetDepth(int d)
    {Depth = d;}
    public String  getTime_elapse() {
        return time_elapse;
    }

    public void setTime_elapse(long start_time_elapse, long end_time_elapse) {
        long diff = end_time_elapse-start_time_elapse;
        long sec = TimeUnit.NANOSECONDS.toMillis(diff);
        this.time_elapse= diff + " ns";

    }
    public CrawlInformation()
    {}
    public CrawlInformation(int e,int d,String t)
    {
        Explorer=e;
        Depth=d;
        time_elapse=t;
    }
}
