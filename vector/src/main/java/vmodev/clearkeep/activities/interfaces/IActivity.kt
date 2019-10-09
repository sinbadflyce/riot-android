package vmodev.clearkeep.activities.interfaces

import androidx.fragment.app.FragmentActivity

interface IActivity {
    fun getActivity(): FragmentActivity;

    companion object {
        const val LOGIN_ACTIVITY = "LOGIN_ACTIVITY";
        const val BACKUP_KEY = "BACKUP_KEY";
        const val RESTORE_BACKUP_KEY = "RESTORE_BACKUP_KEY";
        const val PUSH_BACKUP_KEY = "PUSH_BACKUP_KEY";
        const val WAITING_FOR_VERIFY_EMAIL_ACTIVITY = "WAITING_FOR_VERIFY_EMAIL_ACTIVITY";
        const val PROFILE_ACTIVITY = "PROFILE_ACTIVITY";
        const val PROFILE_SETTINGS_ACTIVITY = "PROFILE_SETTINGS_ACTIVITY";
        const val SEARCH_ACTIVITY = "SEARCH_ACTIVITY";
        const val HOME_SCREEN_ACTIVITY = "HOME_SCREEN_ACTIVITY";
        const val CREATE_NEW_ROOM_ACTIVITY = "CREATE_NEW_ROOM_ACTIVITY";
        const val ROOM_SETTINGS_ACTIVITY = "ROOM_SETTINGS_ACTIVITY";
        const val EDIT_PROFILE_ACTIVITY = "EDIT_PROFILE_ACTIVITY";
        const val MESSAGE_LIST_ACTIVITY = "MESSAGE_LIST_ACTIVITY";
        const val CALL_SETTINGS_ACTIVITY = "CALL_SETTINGS_ACTIVITY";
        const val CHANGE_THEME_ACTIVITY = "CHANGE_THEME_ACTIVITY";
        const val CREATE_NEW_CALL_ACTIVITY = "CREATE_NEW_CALL_ACTIVITY";
        const val DEACTIVATE_ACCOUNT_ACTIVITY = "DEACTIVATE_ACCOUNT_ACTIVITY";
        const val EXPORT_KEY_ACTIVITY = "EXPORT_KEY_ACTIVITY";
        const val NOTIFICATION_SETTINGS_ACTIVITY = "NOTIFICATION_SETTINGS_ACTIVITY";
        const val PRIVACY_POLICY_ACTIVITY = "PRIVACY_POLICY_ACTIVITY";
        const val OTHER_ROOM_SETTINGS_ACTIVITY = "OTHER_ROOM_SETTINGS_ACTIVITY";
        const val ROOM_FILES_LIST_ACTIVITY = "ROOM_FILES_LIST_ACTIVITY";
        const val USER_INFORMATION_ACTIVITY = "USER_INFORMATION_ACTIVITY";
        const val VIEW_USER_PROFILE_ACTIVITY = "VIEW_USER_PROFILE_ACTIVITY";
        const val FIND_AND_CREATE_NEW_CONVERSATION_ACTIVITY = "FIND_AND_CREATE_NEW_CONVERSATION_ACTIVITY";
        const val INVITE_USERS_TO_ROOM_ACTIVITY = "INVITE_USERS_TO_ROOM_ACTIVITY";
        const val NEW_ROOM_ACTIVITY = "NEW_ROOM_ACTIVITY";
        const val SETTINGS_ACTIVITY = "SETTINGS_ACTIVITY";
        const val SHARE_FILE_ACTIVITY = "SHARE_FILE_ACTIVITY";
    }
}