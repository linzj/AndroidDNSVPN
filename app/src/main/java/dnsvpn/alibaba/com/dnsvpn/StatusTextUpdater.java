package dnsvpn.alibaba.com.dnsvpn;

import android.widget.TextView;

/**
 * Created by linzj on 16-11-14.
 */

public class StatusTextUpdater {
    private final TextView textView;
    private StatusGetter statusGetter;

    public StatusTextUpdater(TextView tv) {
        this.textView = tv;
    }

    public void updateToStarted() {
        this.textView.setText("DNS started");
    }

    public void updateToPrepared() {
        this.textView.setText("DNS prepared");
    }

    public void updateToStop() {
        this.textView.setText("DNS stopped");
    }

    public void updateToNotPrepared() {
        this.textView.setText("DNS not prepared");
    }
}
