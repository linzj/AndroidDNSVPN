package dnsvpn.alibaba.com.dnsvpn;

import android.content.Intent;
import android.net.VpnService;

/**
 * Created by linzj on 16-11-14.
 */

public class Toggler implements StatusGetter {


    private final StatusTextUpdater stu;
    private final static int STATUS_PREPARED = 4;
    private int status = STATUS_NOT_PREPARED;

    public Toggler(StatusTextUpdater stu) {
        this.stu = stu;
        stu.updateToNotPrepared();
    }

    public void toggle() {
        switch (status) {
            case STATUS_NOT_PREPARED:
                if (prepare()) {
                    break;
                }
            case STATUS_PREPARED:
                doToggle();
                break;
            default:
                throw new IllegalStateException("toggle");
        }
    }

    private void doToggle() {
        switch (getStatusCode()) {
            case STATUS_PREPARED_STARTED:
                stop();
                break;
            case STATUS_PREPARED_NOT_STARTED:
                start();
                break;
            default:
                throw new IllegalStateException("doToggle");
        }
    }

    private void start() {
        ServiceManager.start();
        stu.updateToStarted();
    }

    private void stop() {
        ServiceManager.stop();
        stu.updateToStop();
    }

    private void updateStatusPrepared() {
        this.status = STATUS_PREPARED;
        stu.updateToPrepared();
    }

    private boolean prepare() {
        if (status == STATUS_NOT_PREPARED) {
            Intent intent = VpnService.prepare(StaticContext.AppContext);
            if (intent != null) {
                StaticContext.AppContext.startActivityForResult(intent, 0);
                return true;
            }
        }
        updateStatusPrepared();
        return false;
    }

    public void prepareResult(int prepareResult) {
        assert status == STATUS_NOT_PREPARED;
        if (prepareResult != 0) {
            updateStatusPrepared();
            doToggle();
            android.util.Log.d("LINZJ", "vpn request okay");
        }
    }

    @Override
    public int getStatusCode() {
        if (status == STATUS_PREPARED) {
            if (ServiceManager.isWorking()) {
                return STATUS_PREPARED_STARTED;
            }
            return STATUS_PREPARED_NOT_STARTED;
        }
        return status;
    }
}
