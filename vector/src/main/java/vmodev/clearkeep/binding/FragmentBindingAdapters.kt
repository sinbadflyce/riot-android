package vmodev.clearkeep.binding

import android.databinding.BindingAdapter
import android.databinding.DataBindingComponent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v4.app.Fragment
import android.support.v7.widget.SwitchCompat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.google.gson.JsonParser
import im.vector.Matrix
import im.vector.R
import im.vector.util.VectorUtils
import org.matrix.androidsdk.rest.model.Event
import vmodev.clearkeep.ultis.toDateTime
import vmodev.clearkeep.viewmodelobjects.Message
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User

class FragmentBindingAdapters constructor(val fragment: Fragment) : ImageViewBindingAdapters, TextViewBindingAdapters, ISwitchCompatViewBindingAdapters {
    override fun bindImage(imageView: ImageView, imageUrl: String?, listener: RequestListener<Drawable?>?) {
        Glide.with(fragment).load(imageUrl).listener(listener).placeholder(R.drawable.ic_launcher_app).into(imageView);
    }

    override fun bindTime(textView: TextView, timeStamp: Long?) {
        timeStamp?.let { l -> fragment.context?.let { context -> textView.text = l.toDateTime(context) } }
    }

    override fun bindStatus(imageView: ImageView, status: Byte?) {
        status?.let { imageView.setImageResource(if (it.compareTo(0) == 0) R.color.main_text_color_hint else R.color.app_green); }
    }

    override fun bindRoomsHighlightCount(textView: TextView, rooms: List<Room>?) {
        var count: Int = 0;
        rooms?.forEach { t: Room? ->
            t?.let {
                count += it.highlightCount
                if (it.type == 1 or 64 || it.type == 2 or 64)
                    count++;
                if (it.notifyCount > 0)
                    count++;
            }
        }
        textView.text = count.toString();
        if (count == 0)
            textView.visibility = View.INVISIBLE;
        else
            textView.visibility = View.VISIBLE
    }

    override fun bindEncrypted(imageView: ImageView, encrypted: Byte?) {
        encrypted?.let { imageView.setImageResource(if (it.compareTo(0) == 0) R.drawable.ic_lock_outline_grey_24dp else R.drawable.ic_lock_outline_green_24dp); }
    }

    override fun bindStatus(switchCompat: SwitchCompat, status: Byte?) {
        status?.let {
            switchCompat.isChecked = status.compareTo(0) != 0
        }
    }

    override fun bindDecryptMessage(textView: TextView, message: Message?) {
        message?.let {
            val session = Matrix.getInstance(fragment.activity!!.applicationContext).defaultSession;
            val parser = JsonParser();
            val event = Event(message?.messageType, parser.parse(message.encryptedContent).asJsonObject, message.userId, message.roomId);
            val result = session.dataHandler.crypto.decryptEvent(event, null);
            result?.let {
                textView.text = result.mClearEvent.toString();
            }
        }

    }

    override fun bindStatusValid(imageView: ImageView, validStatus: Byte?) {
        validStatus?.let { imageView.setImageResource(if (it.compareTo(0) == 0) R.drawable.ic_lock_outline_grey_24dp else R.drawable.ic_lock_outline_green_24dp); }
    }

    override fun bindImage(imageView: ImageView, room: Room?, listener: RequestListener<Drawable?>?) {
        room?.let {
            if (room.avatarUrl.isEmpty()) {
                val bitmap = VectorUtils.getAvatar(imageView.context, VectorUtils.getAvatarColor(room.id), if (room.name.isEmpty()) room.id else room.name, true);
                imageView.setImageBitmap(bitmap);
            } else {
                Glide.with(fragment).load(room.avatarUrl).listener(listener).placeholder(R.drawable.ic_launcher_app).into(imageView);
            }
        }
    }

    override fun bindImage(imageView: ImageView, user: User?, listener: RequestListener<Drawable?>?) {
        user?.let {
            if (user.avatarUrl.isEmpty()) {
                val bitmap = VectorUtils.getAvatar(imageView.context, VectorUtils.getAvatarColor(user.id), if (user.name.isEmpty()) user.id else user.name, true);
                imageView.setImageBitmap(bitmap);
            } else {
                Glide.with(fragment).load(user.avatarUrl).listener(listener).placeholder(R.drawable.ic_launcher_app).into(imageView);
            }
        }
    }
}