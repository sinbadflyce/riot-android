package vmodev.clearkeep.activities

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import im.vector.R
import im.vector.databinding.ActivityOtherRoomSettingsBinding
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.IOtherRoomSettingsActivity

class OtherRoomSettingsActivity : DataBindingDaggerActivity(), IActivity {

    private lateinit var binding: ActivityOtherRoomSettingsBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_other_room_settings);
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener { v ->
            onBackPressed();
        }
        val roomId = intent.getStringExtra(ROOM_ID);
        binding.advancedGroup.setOnClickListener { v ->
            val securityIntent = Intent(this@OtherRoomSettingsActivity, OtherRoomSettingsAdvancedActivity::class.java);
            securityIntent.putExtra(OtherRoomSettingsAdvancedActivity.ROOM_ID, roomId);
            startActivity(securityIntent);
        }
        binding.rolesAndPermissionGroup.setOnClickListener { v ->
            val securityIntent = Intent(this@OtherRoomSettingsActivity, RolesPermissionActivity::class.java);
            startActivity(securityIntent);
        }
        binding.securityAndPrivacyGroup.setOnClickListener { v ->
            val securityIntent = Intent(this@OtherRoomSettingsActivity, SecurityActivity::class.java);
            startActivity(securityIntent);
        }
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    companion object {
        const val ROOM_ID = "ROOM_ID";
    }
}
