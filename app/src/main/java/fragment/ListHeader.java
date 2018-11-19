package fragment;

/**
 * Created by SAI on 4/4/2016.
 */
public class ListHeader {
    String header;
    int newJobCount;

    public ListHeader(String header, int newJobCount) {
        this.header = header;
        this.newJobCount = newJobCount;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getNewJobCount() {
        return newJobCount;
    }

    public void setNewJobCount(int newJobCount) {
        this.newJobCount = newJobCount;
    }

    @Override
    public String toString() {
        return header;
    }
}
