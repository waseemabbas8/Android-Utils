package com.softsolutions.ilamkidunya.css.ui.posting

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.model.MediaFile
import com.softsolutions.ilamkidunya.css.R
import com.softsolutions.ilamkidunya.css.data.FILE_SELECT_VALUE
import com.softsolutions.ilamkidunya.css.data.IMAGE_SELECT_VALUE
import com.softsolutions.ilamkidunya.css.data.MAX_FILE_SIZE
import com.softsolutions.ilamkidunya.css.data.VIDEO_SELECT_VALUE
import com.softsolutions.ilamkidunya.css.utils.*
import java.io.File
import java.util.*

class PostDialogFragment(private val isReplyPost: Boolean = false) : DialogFragment() {

    private lateinit var mCommentMessage: EditText
    private lateinit var mPhoto: ImageView
    private lateinit var mPhotoContainer: LinearLayout
    private lateinit var mVideo: ImageView
    private lateinit var mAttachment: ImageView
    private lateinit var mAttachmentContainer: LinearLayout
    private lateinit var mMcq: ImageView
    private lateinit var mFileName: TextView
    private lateinit var mUploadIcon: ImageView
    private lateinit var mSubmitPost: Button

    private var mFile: File? = null
    private var selectValue = 0

    private lateinit var listener: PostDialogListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_post_comment, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mCommentMessage = view.findViewById(R.id.txt_msg)
        mSubmitPost = view.findViewById(R.id.btn_post)
        mPhoto = view.findViewById(R.id.photo)
        mVideo = view.findViewById(R.id.video)
        mAttachment = view.findViewById(R.id.attachment)
        mMcq = view.findViewById(R.id.mcqs)
        mFileName = view.findViewById(R.id.txt_file_name)
        mUploadIcon = view.findViewById(R.id.upload_icon)
        mPhotoContainer = view.findViewById(R.id.photo_container)
        mAttachmentContainer = view.findViewById(R.id.attachment_container)

        if (isReplyPost) { hideAttachOptions() }

        mPhoto.setOnClickListener {
            FilePicker(this).start(REQUEST_IMAGE)
            selectValue = IMAGE_SELECT_VALUE
        }

        mVideo.setOnClickListener {
            FilePicker(this).start(REQUEST_VIDEO)
            selectValue = VIDEO_SELECT_VALUE
        }

        mAttachment.setOnClickListener {
            FilePicker(this).start(REQUEST_FILE)
            selectValue = FILE_SELECT_VALUE
        }

        mMcq.setOnClickListener {}

        mSubmitPost.setOnClickListener {
            val isImageAttached = mFile != null && selectValue == IMAGE_SELECT_VALUE
            if (mCommentMessage.text.isNullOrEmpty() && !isImageAttached) {
                mCommentMessage.error = getString(R.string.msg_comment_empty_error)
                return@setOnClickListener
            }

            listener.onClickPost(mFile, "", mCommentMessage.text.toString(), selectValue)

            dismiss()
        }

    }

    private fun hideAttachOptions() {
        mAttachmentContainer.visibility = View.GONE
        mPhotoContainer.visibility = View.GONE
    }

    fun setPostDialogListener(postDialogListener: PostDialogListener) {
        this.listener = postDialogListener
    }

    interface PostDialogListener {
        fun onClickPost(file: File?, youTubeLink: String, comment: String, fileType: Int)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {

                REQUEST_IMAGE, REQUEST_VIDEO, REQUEST_FILE -> {
                    val files: ArrayList<MediaFile>? = data?.getParcelableArrayListExtra(
                        FilePickerActivity.MEDIA_FILES
                    )
                    if (files.isNullOrEmpty()) return
                    val selectedFile = files[0]
                    if (selectedFile.size >= MAX_FILE_SIZE) {
                        Toast.makeText(context, "File size must be less than 4 MB", Toast.LENGTH_SHORT).show()
                        return
                    }
                    mFile = Helpers.FileHelper.createSelectedFileCopy(selectedFile.uri, context!!)
                    mFileName.text = selectedFile.name
                    mFileName.visibility = View.VISIBLE
                    mUploadIcon.visibility = View.VISIBLE
                }
            }
        }

    }

}