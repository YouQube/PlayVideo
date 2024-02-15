package com.brandon.playvideo_app.ui.library

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.WindowInsetsControllerCompat
import com.brandon.playvideo_app.R
import com.brandon.playvideo_app.databinding.EditProfileActivityBinding
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class EditProfileActivity() : AppCompatActivity() {
    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var _binding: EditProfileActivityBinding? = null
    private val binding get() = _binding!!

    private val sharedPreferences by lazy {
        getSharedPreferences(LibraryFragment.KEY_PREFS, Context.MODE_PRIVATE)
    }

    var imgName = "profile_image.png"
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
        const val KEY_PREFS = "profile_setting"
        const val KEY_IMAGE = "profile_image"
        const val KEY_NAME = "profile_name"
        const val KEY_DESC = "profile_description"
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
        _binding = EditProfileActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timber.plant(Timber.DebugTree())
        window.apply {
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
            statusBarColor = Color.BLACK
            WindowInsetsControllerCompat(this, this.decorView).isAppearanceLightStatusBars = true

            with(binding) {
                loadUserProfile()


                ivEditProfileProfile.setOnClickListener {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }

                btnEditProfileSave.setOnClickListener {
                    saveUserProfile()
                }
                btnEditProfileCancel.setOnClickListener {
                    onBackPressed()
                }
            }
        }

        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    result ->
                if (result.resultCode == RESULT_OK && result.data != null) {
                    val fileUri = result.data!!.data
                    val resolver = contentResolver
                    try {
                        val instream = resolver.openInputStream(fileUri!!)
                        val imgBitmap = BitmapFactory.decodeStream(instream)
                        binding.ivEditProfileProfile.setImageBitmap(imgBitmap) // 선택한 이미지 이미지뷰에 셋
                        instream!!.close() // 스트림 닫아주기
                        saveBitmapToJpeg(imgBitmap) // 내부 저장소에 저장
                        Toast.makeText(applicationContext, "파일 불러오기 성공", Toast.LENGTH_SHORT).show()
                    } catch (e: java.lang.Exception) {
                        Toast.makeText(applicationContext, "파일 불러오기 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun saveUserProfile() {
        val stream = ByteArrayOutputStream()
        val profileImage = findViewById<ImageView>(R.id.iv_edit_profile_profile).drawable
        profileImage.toBitmap()
            .compress(Bitmap.CompressFormat.PNG, 100, stream) // Drawable을 Bitmap으로 변환
        val byteArray = stream.toByteArray()
        val editor = sharedPreferences.edit()
        val profileName = findViewById<EditText>(R.id.et_edit_profile_name)
        val profimeDescription = findViewById<EditText>(R.id.et_edit_profile_description)
        editor.putString(
            LibraryFragment.EXTRA_STORE_PROFILE_IMAGE,
            Base64.encodeToString(byteArray, Base64.DEFAULT)
        ) // Bitmap을 Base64 문자열로 인코딩
        editor.putString("name", profileName.text.toString())
        editor.putString("description", profimeDescription.text.toString())
        editor.apply()
    }

    private fun loadUserProfile() {
        val storedProfileImage = sharedPreferences.getString(LibraryFragment.EXTRA_STORE_PROFILE_IMAGE, "")
        if (storedProfileImage != "") {
            val byteArray =
                Base64.decode(storedProfileImage, Base64.DEFAULT) // Base64 문자열을 Bitmap으로 디코딩
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            with(binding) {
                ivEditProfileProfile.setImageBitmap(bitmap) // Bitmap을 이용해 이미지 띄우기
                etEditProfileName.setText(sharedPreferences.getString("name", ""))
                etEditProfileDescription.setText(sharedPreferences.getString("description", ""))
            }
        }
    }

    private fun saveBitmapToJpeg(bitmap: Bitmap) {   // 선택한 이미지 내부 저장소에 저장
        val tempFile = File(cacheDir, imgName) // 파일 경로와 이름 넣기
        Log.d(TAG, "saveBitmapToJpeg: $cacheDir")
        try {
            tempFile.createNewFile() // 자동으로 빈 파일을 생성하기
            val out = FileOutputStream(tempFile) // 파일을 쓸 수 있는 스트림을 준비하기
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out) // compress 함수를 사용해 스트림에 비트맵을 저장하기
            out.close() // 스트림 닫아주기
            Toast.makeText(applicationContext, "파일 저장 성공", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "파일 저장 실패", Toast.LENGTH_SHORT).show()
        }
    }
}