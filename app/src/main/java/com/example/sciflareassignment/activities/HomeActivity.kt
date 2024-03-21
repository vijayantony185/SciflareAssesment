package com.example.sciflareassignment.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sciflareassignment.R
import com.example.sciflareassignment.adapter.UserAdapter
import com.example.sciflareassignment.data.User
import com.example.sciflareassignment.data.UsersDao
import com.example.sciflareassignment.data.UsersDataBase
import com.example.sciflareassignment.data.UsersViewModel
import com.example.sciflareassignment.data.UsersViewModelFactory
import com.example.sciflareassignment.databinding.ActivityHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var usersViewModel: UsersViewModel
    private var usersDao: UsersDao? = null
    private val locationPermissionRequestCode = 1000
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        usersViewModel = ViewModelProvider(this, UsersViewModelFactory())[UsersViewModel::class.java]
        usersViewModel.getUsers()
        try {
            usersDao = UsersDataBase.getInstance(this).userDao()
        } catch (e: Exception) {
           e.printStackTrace()
        }
        getUsers()
        binding.fab.setOnClickListener {
            checkPermission()
        }
    }

    private fun getUsers() {
        if (isInternetConnected()) {
            usersViewModel.userDetails.observe(this) {
                lifecycleScope.launch {
                    it.data?.let {
                        usersDao?.getUsers()?.let { list ->
                            usersDao?.deleteUsers(list)
                        }
                        for (user in it) {
                            usersDao?.insert(user)
                        }
                        initRecyclerview(usersDao?.getUsers())
                    }
                }
            }
        } else {
            lifecycleScope.launch {
                initRecyclerview(usersDao?.getUsers())
            }
        }
    }

    private fun initRecyclerview(list: List<User>?) {
        list?.let {
            binding.imgEmpty.isVisible = it.isEmpty()
            binding.rcvUsers.adapter = UserAdapter(this@HomeActivity, ArrayList(it))
            binding.rcvUsers.layoutManager = LinearLayoutManager(this)
        }

    }

    private fun isInternetConnected(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val network = connectivityManager.activeNetwork
            val capablities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return capablities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            @Suppress("DEPRECATION") val networkInfo = connectivityManager.activeNetworkInfo
            @Suppress("DEPRECATION") return networkInfo?.isConnectedOrConnecting ?: false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionRequestCode) {
            var fineLocationGranted = false
            var coarseLocationGranted = false
            for (i in permissions.indices) {
                if (permissions[i] == android.Manifest.permission.ACCESS_FINE_LOCATION && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    fineLocationGranted = true
                }
                if (permissions[i] == android.Manifest.permission.ACCESS_COARSE_LOCATION && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    coarseLocationGranted = true
                }
            }
            if (fineLocationGranted && coarseLocationGranted) {
                // Both fine and coarse location permissions granted, proceed to get the location
                getCurrentLocation()
            } else {
                showPermissionDeniedDialog()
            }
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ), locationPermissionRequestCode
            )
        } else {
            getCurrentLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                // Got last known location. Use it
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val intent = Intent(this@HomeActivity, MapsActivity::class.java)
                    intent.putExtra("Latitude", latitude)
                    intent.putExtra("Longitude", longitude)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this,
                        "Kindly check your internet connection and location permission status",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    private fun showPermissionDeniedDialog(){
        AlertDialog.Builder(this)
            .setTitle("Location Permission Required")
            .setMessage("This app requires location permission to function properly.")
            .setPositiveButton("Grant Permission") { dialog, _ ->
                dialog.dismiss()
                val intent = Intent().apply {
                    action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", packageName, null)
                }
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}