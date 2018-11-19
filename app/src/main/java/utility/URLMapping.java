package utility;

/**
 * Created by SAI on 4/4/2016.
 */
public class URLMapping {
    private String []paramNames;
    private String url;
    private boolean needToSync;

    public URLMapping(String[] paramNames, String url, boolean needToSync) {
        this.paramNames = paramNames;
        this.url = url;
        this.needToSync = needToSync;
    }

    public String[] getParamNames() {
        return paramNames;
    }

    public void setParamNames(String[] paramNames) {
        this.paramNames = paramNames;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isNeedToSync() {
        return needToSync;
    }

    public void setNeedToSync(boolean needToSync) {
        this.needToSync = needToSync;
    }
}
