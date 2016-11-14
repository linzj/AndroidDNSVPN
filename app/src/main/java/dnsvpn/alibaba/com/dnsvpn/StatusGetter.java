package dnsvpn.alibaba.com.dnsvpn;

/**
 * Created by linzj on 16-11-14.
 */

public interface StatusGetter {
    public final static int STATUS_NOT_PREPARED = 0;
    public final static int STATUS_PREPARED_NOT_STARTED = 1;
    public final static int STATUS_PREPARED_STARTED = 2;
    int getStatusCode();
}
