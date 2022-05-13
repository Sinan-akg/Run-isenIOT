package com.example.runisenapp.ble

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runisenapp.R
import com.example.runisenapp.databinding.ActivityBleDeviceBinding
import java.util.*

@SuppressLint("MissingPermission")
class BleDeviceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBleDeviceBinding
    private var bluetoothGatt: BluetoothGatt? = null
    private var timer: Timer? = null
    private lateinit var adapter: BleServiceAdapter


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityBleDeviceBinding.inflate(layoutInflater)
        setContentView((binding.root))

        val imageClick5 = findViewById<ImageView>(R.id.backButtonn)
        imageClick5.setOnClickListener {
            val intent = Intent(this, BluetoothActivity::class.java)
            startActivity(intent)
        }


        val device = intent.getParcelableExtra<BluetoothDevice?>(BluetoothActivity.DEVICE_KEY)
        Toast.makeText(this, device?.address, Toast.LENGTH_SHORT).show()
        binding.deviceName.text = device?.name ?: "Nom inconnu"
        binding.deviceStatus.text = getString(R.string.ble_device_disconnected)

        connectToDevice(device)
    }

    override fun onStop() {
        super.onStop()
        closeBluetoothGatt()
    }

    private fun closeBluetoothGatt() {
        bluetoothGatt?.close()
        bluetoothGatt = null
    }


    private fun connectToDevice(device: BluetoothDevice?) {
        this.bluetoothGatt = device?.connectGatt(this, true, gattCallback)
        this.bluetoothGatt?.connect()
    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            when (newState) {
                BluetoothGatt.STATE_CONNECTED -> {
                    gatt?.discoverServices()
                    runOnUiThread { binding.deviceStatus.text = "Connected" }
                }
                BluetoothGatt.STATE_CONNECTING -> {
                    runOnUiThread { binding.deviceStatus.text = "Connection..." }
                }
                else -> {
                    runOnUiThread { binding.deviceStatus.text = "No connection" }
                }
            }
        }


        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)

            val bleServices =
                gatt?.services?.map { BleService(it.uuid.toString(), it.characteristics) }
                    ?: arrayListOf()
            runOnUiThread {
                binding.serviceList.layoutManager = LinearLayoutManager(this@BleDeviceActivity)


                adapter = BleServiceAdapter(bleServices,
                    this@BleDeviceActivity,
                    { characteristic -> gatt?.readCharacteristic(characteristic) },
                    { characteristic -> writeIntoCharacteristic(gatt!!, characteristic) },
                    { characteristic, enable ->
                        toggleNotificationOnCharacteristic(
                            gatt!!,
                            characteristic,
                            enable
                        )
                    }
                )
                binding.serviceList.adapter = adapter
            }

            Log.d("BluetoothLeService", "onServicesDiscovered()")
            val gattServices: List<BluetoothGattService> = gatt!!.services
            Log.d("onServicesDiscovered", "Count service: " + gattServices.size)
            for (gattService in gattServices) {
                val serviceUUID = gattService.uuid.toString()
                Log.d("onServicesDiscovered", "UUID service $serviceUUID")

                val gattChara: List<BluetoothGattCharacteristic> = gattService.characteristics
                for (gattC in gattChara) {
                    val charaUUID = gattC.uuid.toString()
                    Log.d("onServicesDiscovered", "        UUID character $charaUUID")
                }
            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int
        ) {
            super.onCharacteristicRead(gatt, characteristic, status)
            runOnUiThread {
                adapter.updateFromChangedCharacteristic(characteristic)
                adapter.notifyDataSetChanged()
            }
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?
        ) {
            super.onCharacteristicChanged(gatt, characteristic)
        }

        private fun toggleNotificationOnCharacteristic(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            enable: Boolean
        ) {
            characteristic.descriptors.forEach {
                it.value =
                    if (enable) BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE else BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
                gatt.writeDescriptor(it)
            }
            gatt.setCharacteristicNotification(characteristic, enable)
            timer = Timer()
            timer?.schedule(object : TimerTask() {
                override fun run() {
                    gatt.readCharacteristic(characteristic)
                }
            }, 0, 1000)
        }


        private fun writeIntoCharacteristic(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            runOnUiThread {
                val input = EditText(this@BleDeviceActivity)
                input.inputType = InputType.TYPE_CLASS_TEXT
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(16, 0, 16, 0)
                input.layoutParams = params

                AlertDialog.Builder(this@BleDeviceActivity)
                    .setTitle("Write value")
                    .setView(input)
                    .setPositiveButton("Validate") { _, _ ->
                        characteristic.value = input.text.toString().toByteArray()
                        gatt.writeCharacteristic(characteristic)
                        gatt.readCharacteristic(characteristic)
                    }
                    .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
                    .create()
                    .show()
            }
        }
    }
}