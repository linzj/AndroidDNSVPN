package dnsvpn.alibaba.com.dnsvpn;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StaticContext.AppContext = this;
        View v = this.findViewById(R.id.activity_main);
        v.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!ServiceManager.isWorking())
                    ServiceManager.start();
            }
        });

    }
}
