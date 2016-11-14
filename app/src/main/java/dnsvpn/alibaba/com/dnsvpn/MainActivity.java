package dnsvpn.alibaba.com.dnsvpn;

import android.app.Activity;
import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
    Toggler toggler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StaticContext.AppContext = this;
        View v = this.findViewById(R.id.activity_main);
        v.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                toggler.toggle();
            }
        });
        StatusTextUpdater stu = new StatusTextUpdater((TextView)v.findViewById(R.id.mytextview));
        toggler = new Toggler(stu);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        switch (reqCode) {
            case 0:
                toggler.prepareResult(resultCode);
                break;
        }
    }
}
