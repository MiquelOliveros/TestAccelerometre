package udl.eps.testaccelerometre;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class TestAccelerometreActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private boolean color = false;
    private TextView viewSuperior;
    private TextView viewMig;
    private TextView viewInferior;
    private long lastUpdate;

    private boolean sensor;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        findViews();

        viewSuperior.setBackgroundColor(Color.GREEN);
        viewInferior.setBackgroundColor(Color.YELLOW);

//        PackageManager manager = getPackageManager();
//        sensor = manager.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (sensorManager != null) {
            Sensor accelerometre = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            Sensor luminic = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

            if (accelerometre != null){
                sensorManager.registerListener(this,
                        accelerometre,
                        SensorManager.SENSOR_DELAY_NORMAL);

                String showCapabilities = "";

                showCapabilities = showCapabilities + getText(R.string.shake) +
                        "\nMax Delay: " + accelerometre.getMaxDelay() +
                        "\nMin Delay: " + accelerometre.getMinDelay() +
                        "\nPower: " + accelerometre.getPower() +
                        "\nResolution: " + accelerometre.getResolution() +
                        "\nVersion: " + accelerometre.getVersion();

                viewMig.setText(showCapabilities);

            } else {
                viewMig.setText(getText(R.string.noAccel));
            }

//            if (luminic != null){
//                sensorManager.registerListener(this,
//                        luminic,
//                        SensorManager.SENSOR_DELAY_NORMAL);
//            } else {
//                //TODO --> si no existeix el sensor luminic
//            }



        }
        // register this class as a listener for the accelerometer sensor

        lastUpdate = System.currentTimeMillis();

    }

    private void findViews() {
        viewSuperior = findViewById(R.id.tvSuperior);
        viewMig = findViewById(R.id.tvMig);
        viewInferior = findViewById(R.id.tvInferior);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        getAccelerometer(event);
//        getLuminic(event);
    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = System.currentTimeMillis();
        if (accelationSquareRoot >= 2)
        {
            if (actualTime - lastUpdate < 200) {
                return;
            }
            lastUpdate = actualTime;

            Toast.makeText(this, R.string.shuffed, Toast.LENGTH_SHORT).show();
            if (color) {
                viewSuperior.setBackgroundColor(Color.GREEN);

            } else {
                viewSuperior.setBackgroundColor(Color.RED);
            }
            color = !color;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}