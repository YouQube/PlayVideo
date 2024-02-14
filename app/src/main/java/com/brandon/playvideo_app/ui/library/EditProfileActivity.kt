package com.brandon.playvideo_app.ui.library

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.addTextChangedListener
import com.brandon.playvideo_app.databinding.EditProfileActivityBinding
import timber.log.Timber

interface OkClick {
    fun onClick(profileImage: Drawable, name: String, description: String)
}

class EditProfileActivity : AppCompatActivity() {

    var okClick: OkClick? = null

    private var _binding: EditProfileActivityBinding? = null
    private val binding get() = _binding!!

    private var selectedProfile: Uri? = null
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.ivEditProfileProfile.setImageURI(uri)
            binding.ivEditProfileProfile.scaleType = ImageView.ScaleType.CENTER_CROP
            selectedProfile = uri
        }
    }

    companion object {

        const val NOTE_BOTTOM_SHEET_TAG = "Note Bottom Sheet Fragment"
        const val SELECTED_COLOR = "selectedColor"

//        @JvmStatic
//        fun newInstance() =
//            EditMyProfileActivity(userInfo = ).apply {
//                arguments = Bundle().apply {
//                }
//            }
    }

    private val editTexts
        get() = listOf(
            binding.etEditProfileName,
            binding.etEditProfileDescription
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        Timber.plant(Timber.DebugTree())
        window.apply {
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
            statusBarColor = Color.BLACK
            WindowInsetsControllerCompat(this, this.decorView).isAppearanceLightStatusBars = true
        }
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        with(binding) {
//            btnEditProfileCheck.isEnabled = false  // 버튼을 비활성화 시킴
//
//            ivEditProfileProfile.setImageDrawable(userProfileImage)
//            ivEditProfileProfile.scaleType = ImageView.ScaleType.CENTER_CROP
//            etEditProfileName.setText(userInfo[0])
//            etEditProfileDescription.setText(userInfo[1])
//
//            setAddButtonEnable()
//            setTextChangeLisener()
//            setFocusChangedLisener()
//
//            btnEditProfileCancel.setOnClickListener {
//                dismiss()
//            }
//
//            ivEditProfileProfile.setOnClickListener {
//                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//            }
//
//            btnEditProfileCheck.setOnClickListener {
//                // 마이페이지프래그먼트로 데이터 넘기기
//                okClick?.onClick(
//                    ivEditProfileProfile.drawable,
//                    etEditProfileName.text.toString(),
//                    etEditProfileDescription.text.toString())
//                dismiss()
//            }
//        }
//    }

    // 전체적으로 에러가 있는지 여부를 판단해 에러가 있는 경우(또는 아무것도 없는 경우) 버튼을 비활성화 시키는 함수
    private fun setTextChangeLisener(){
        editTexts.forEach{editText ->
            editText.addTextChangedListener {
                editText.setErrorMessage()
                setAddButtonEnable()
            }
        }
        binding.etEditProfileDescription.addTextChangedListener(PhoneNumberFormattingTextWatcher())
    }

    private fun setFocusChangedLisener() {
        editTexts.forEach { editText ->
            editText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus.not()) {
                    editText.setErrorMessage()
                    setAddButtonEnable()
                }
            }
        }
    }

    // 만약에 입력받은 코드에 error가 날 경우 각각 에러메세지가 있는 함수를 실행
    private fun EditText.setErrorMessage(){
        when(this) {
            binding.etEditProfileName -> error = getMessageValidName()
            binding.etEditProfileDescription -> error = getMessageValidDescription()
        }
    }

    // 이름부분에 에러메세지 출력하는 함수
    private fun getMessageValidName() : String? {
        val name = binding.etEditProfileName.text.toString()
        return when{
            name.isBlank() -> EditProfileErrorMessage.EMPTY_NAME //이름 부분이 공백이면 AddContactErrorMessage에 EMPTY_NAME을 불러와 메세지를 띄운다.
            else -> null
        }?.message?.let{ getString(it) }
    }



    //이메일에 에러메시지를 출력하는 함수
    private fun getMessageValidDescription() : String?{
        val email = binding.etEditProfileDescription.text.toString()
        return when{
            email.isBlank() -> EditProfileErrorMessage.EMPTY_DESCRIPTION //이메일칸이 공백일 때 실행
            else -> null
        }?.message?.let{getString(it)}
    }

    //버튼을 활성화하는 함수
    private fun setAddButtonEnable() {
        binding.btnEditProfileCheck.isEnabled = getMessageValidName() == null
                && getMessageValidDescription() == null
    }
}