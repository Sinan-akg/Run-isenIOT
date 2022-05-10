package com.example.runisenapp.ble

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION_CODES.S
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runisenapp.ProfilePage
import com.example.runisenapp.R
import com.example.runisenapp.databinding.ActivityBluetoothBinding
import androidx.core.view.isVisible
import com.example.runisenapp.HomeActivity

class BluetoothActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBluetoothBinding
    private var isScanning = false
    private val ENABLE_BLUETOOTH_REQUEST_CODE = 1
    private val ALL_PERMISSION_REQUEST_CODE = 10
    private lateinit var adapter: BLEAdapter


    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)

        val imageClick10 = findViewById<ImageView>(R.id.backButton)
        imageClick10.setOnClickListener {
            val intent = Intent(this, ProfilePage::class.java)
            startActivity(intent)
        }

        binding = ActivityBluetoothBinding.inflate(layoutInflater)
        setContentView(binding.root)

        when {
            bluetoothAdapter?.isEnabled == true ->
                binding.bleScanStateImg.setOnClickListener {
                    startLeScanBLEWithPermission(!isScanning)
                    startLeScanBLEWithPermission(true)
                }

            bluetoothAdapter != null ->
                askBluetoohPermission()
            else -> {
                displayBLEUnavailable()
            }

        }

        binding.bleScanStateImg.setOnClickListener {
            startLeScanBLEWithPermission(!isScanning)
        }
        binding.bleScanStateTitle.setOnClickListener {
            startLeScanBLEWithPermission(!isScanning)
        }

        adapter = BLEAdapter(arrayListOf()) {
            val intent = Intent(this, BleDeviceActivity::class.java)
            intent.putExtra(DEVICE_KEY, it)
            startActivity(intent)
        }
        binding.listeitem.layoutManager = LinearLayoutManager(this)
        binding.listeitem.adapter = adapter

    }


    private fun checkAllPermissionGranted(): Boolean {
        return getAllPermissions().all { permission ->
            val test = ActivityCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
            return@all test
        }
    }

    private fun getAllPermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.BLUETOOTH_SCAN,
                android.Manifest.permission.BLUETOOTH_CONNECT
            )
        } else {
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }



    private fun startLeScanBLEWithPermission(enable: Boolean) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION // si on a bien activé la localisation
            ) == PackageManager.PERMISSION_GRANTED //ok
        )
         else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.BLUETOOTH_CONNECT, //connexion (android 12)
                    Manifest.permission.BLUETOOTH_SCAN //scan (android 12)

                ), ALL_PERMISSION_REQUEST_CODE
            )


        }
        startLeScanBLE(enable)
    }


    @SuppressLint("MissingPermission")
    private fun startLeScanBLE(enable: Boolean) {

        bluetoothAdapter?.bluetoothLeScanner?.apply {
            if (enable) {
                isScanning = true
                startScan(scanCallBack)
            } else {
                isScanning = false
                stopScan(scanCallBack)
            }
            handlePlayPauseAction()
        }
    }


    private val scanCallBack = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            Log.d("BLEScanActivity", "result : ${result.device?.address}, rssi : ${result.rssi}")

            adapter.apply {
                addElement(result)
                notifyDataSetChanged()
            }

        }
    }

    private fun displayBLEUnavailable() {
        binding.bleScanStateImg.isVisible = false
        binding.bleScanStateTitle.text = getString(R.string.ble_scan_error)
        binding.progressBar.isIndeterminate = false
    }

    private fun askBluetoohPermission() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH_REQUEST_CODE)
        }
    }

    private fun handlePlayPauseAction() {
        if (isScanning) {
            binding.bleScanStateImg.setImageResource(R.drawable.ic_pause)
            binding.bleScanStateTitle.text = getString(R.string.ble_scan_pause)
            binding.progressBar.isIndeterminate = true
            binding.progressBar.isVisible = true
        } else {
            binding.bleScanStateImg.setImageResource(R.drawable.ic_play)
            binding.bleScanStateTitle.text = getString(R.string.ble_scan_play)
            binding.progressBar.isIndeterminate = true
            binding.progressBar.isVisible = false
        }
    }

    companion object {
        private const val ENABLE_BLUETOOTH_REQUEST_CODE = 1
        private const val ALL_PEMISSION_REQUEST_CODE = 100
        const val DEVICE_KEY = "Device"
    }
}