package util;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by sissy on 14/5/2017.
 */

public class ErrorActivity extends AppCompatActivity
{
    public void PrintError(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }
}
