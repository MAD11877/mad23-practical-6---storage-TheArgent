package sg.edu.np.mad.practical6;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    final String TAG = "Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve the random number from the intent
        Intent intent = getIntent();
        int userId = intent.getIntExtra("userId", -1); // Assuming you have the user ID as an extra in the intent

        TextView username = findViewById(R.id.username);
        TextView description = findViewById(R.id.description);
        Button followButton = findViewById(R.id.follow);

        // Update the user "followed" in the database
        MyDBHandler dbHandler = new MyDBHandler(MainActivity.this);
        User user = dbHandler.getUserById(userId);

        if (user != null) {
            // Update the User object with the retrieved values
            username.setText(user.getName());
            description.setText(user.getDescription());
            followButton.setText(user.isFollowed() ? "Unfollow" : "Follow");
        } else {
            // Handle the case when the user is not found in the database
            Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_SHORT).show();
        }

        // Add click listener to the follow button
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reverse the variable of followed
                user.setFollowed(!user.isFollowed());
                // Update the text of the follow button
                followButton.setText(user.isFollowed() ? "Unfollow" : "Follow");

                // Show toast message
                Toast.makeText(getApplicationContext(), user.isFollowed() ? "Followed" : "Unfollowed", Toast.LENGTH_SHORT).show();

                // Update the user "followed" in the database
                dbHandler.updateUser(user);

                // Pass the updated user data back to ListActivity to retrieve the updated database
                Intent resultIntent = new Intent();
                resultIntent.putExtra("position", intent.getIntExtra("position", -1));
                resultIntent.putExtra("followed", user.isFollowed());
                setResult(RESULT_OK, resultIntent);

                Log.d(TAG, "User updated: " + user.getName() + " - Followed: " + user.isFollowed());
            }
        });
    }
}
