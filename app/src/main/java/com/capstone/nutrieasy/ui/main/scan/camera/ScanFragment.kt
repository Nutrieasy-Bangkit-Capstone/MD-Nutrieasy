package com.capstone.nutrieasy.ui.main.scan.camera

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.capstone.nutrieasy.R
import com.capstone.nutrieasy.databinding.FragmentScanBinding
import com.capstone.nutrieasy.util.createCustomTempFile
import com.capstone.nutrieasy.util.isPermissionGranted
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ScanFragment : Fragment() {
    private lateinit var binding: FragmentScanBinding
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null

    private val requestCameraPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){ isGranted ->
        if(!isGranted){
            showToast("Permissions rejected")
            findNavController().navigate(ScanFragmentDirections.actionGlobalNavigationHome())
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestingPermission()
        setUpView()
        setUpAction()
    }

    private fun setUpView(){
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility = View.GONE
//        val window = requireActivity().window
//        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.black)
    }

    private fun requestingPermission(){
        when{
            isPermissionGranted(requireContext(), Manifest.permission.CAMERA) -> showToast("Camera permission granted")
            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(), Manifest.permission.CAMERA
            ) -> {
                val dialog = MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Permission Required")
                    .setMessage("This app requires CAMERA permission for scanning the food")
                    .setIcon(R.drawable.photo_camera_24px)
                    .setNegativeButton("Decline") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton("Accept") { dialog, _ ->
                        requestCameraPermission.launch(Manifest.permission.CAMERA)
                        dialog.dismiss()
                    }
                dialog.show()
            }
            else -> requestCameraPermission.launch(Manifest.permission.CAMERA)
        }
    }

    private fun setUpAction(){
        binding.switchBtn.setOnClickListener {
            cameraSelector = if(cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA){
                CameraSelector.DEFAULT_FRONT_CAMERA
            }else CameraSelector.DEFAULT_BACK_CAMERA
            showToast("Test")
            startCamera()
        }

        binding.captureBtn.setOnClickListener {
            takePhoto()
        }
    }

    private fun startCamera(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector,
                    preview, imageCapture
                )
            }catch(exc: Exception){
                showToast(getString(R.string.camera_failed))
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto(){
        val imageCapture = imageCapture ?: return
        val photoFile = createCustomTempFile(requireContext())
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object: ImageCapture.OnImageSavedCallback{
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val directions = ScanFragmentDirections.actionScanFragmentToResultFragment(output.savedUri.toString())
                    findNavController().navigate(directions)
                }

                override fun onError(exception: ImageCaptureException) {
                    showToast(getString(R.string.camera_failed))
                }
            }
        )
    }

    private fun showToast(text: String){
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        startCamera()
    }

    override fun onStart() {
        super.onStart()
        orientationEventListener.enable()
    }

    override fun onStop() {
        super.onStop()
        orientationEventListener.disable()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility = View.VISIBLE
    }

    private val orientationEventListener by lazy {
        object: OrientationEventListener(requireContext()){
            override fun onOrientationChanged(orientation: Int) {
                if(orientation == ORIENTATION_UNKNOWN){
                    return
                }

                val rotation = when(orientation){
                    in 45 until 135 -> Surface.ROTATION_270
                    in 135 until 225 -> Surface.ROTATION_180
                    in 225 until 315 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }

                imageCapture?.targetRotation = rotation
            }
        }
    }
}