package com.example.runisenapp

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import com.example.runisenapp.databinding.ActivitySemelleBinding
import java.util.*

class SemelleActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySemelleBinding
    private val ENABLE_BLUETOOTH_REQUEST_CODE = 1
    private val ALL_PERMISSION_REQUEST_CODE = 100
    private var timer: Timer? = null
    private var comptage1 = 0
    private var comptage2 = 0
    private var comptage3 = 0
    private var comptage4 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySemelleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageClick3 = findViewById<ImageView>(R.id.backButtonn)
        imageClick3.setOnClickListener {
            val intent = Intent(this, ProfilePage::class.java)
            startActivity(intent)
        }
        val bluetoothAdapter: BluetoothAdapter? by lazy {
            val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            bluetoothManager.adapter
        }
        val device: BluetoothDevice? = bluetoothAdapter?.getRemoteDevice("00:80:E1:26:6F:6E")

    //"00:80:E1:26:2C:46" Runisen
    // "00:80:E1:26:72:58" babisen
    //"00:80:E1:26:6F:6E" runisen 2

        when {
            bluetoothAdapter?.isEnabled == true ->
                connectToDevice(device)
            bluetoothAdapter != null ->
                askBluetoothPermission()
            else -> {
            }
        }
    }

    private fun connectToDevice(device: BluetoothDevice?) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            device?.connectGatt(this, true, object : BluetoothGattCallback() {
                @SuppressLint("MissingPermission")
                override fun onConnectionStateChange(
                    gatt: BluetoothGatt?,
                    status: Int,
                    newState: Int
                ) {
                    super.onConnectionStateChange(gatt, status, newState)
                    if (newState == BluetoothProfile.STATE_CONNECTED) {
                        Log.d("BleNotif", "Connecté")
                        gatt!!.discoverServices()
                    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                        Log.d("BleNotif", "Pas connecté")
                    }
                }

                @SuppressLint("MissingPermission")
                override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {

                    //gatt!!.setCharacteristicNotification(gatt.services[2].characteristics[1], true)
                    Log.d("BleNotif", "je découvre")

                    gatt!!.services[2].characteristics[1].descriptors.forEach {
                        it.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        gatt.writeDescriptor(it)
                    }

                    gatt.setCharacteristicNotification(gatt.services[2].characteristics[1], true)


                    /*gatt.services[2].characteristics[0].descriptors.forEach {
                        it.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        gatt.writeDescriptor(it)
                    }
                    gatt.setCharacteristicNotification(gatt.services[2].characteristics[0], true)*/

                    /*gatt.services[2].characteristics[2].descriptors.forEach {
                        it.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        gatt.writeDescriptor(it)
                    }

                    gatt.setCharacteristicNotification(gatt.services[2].characteristics[2], true)


                    gatt.services[2].characteristics[3].descriptors.forEach {
                        it.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        gatt.writeDescriptor(it)
                    }

                    gatt.setCharacteristicNotification(gatt.services[2].characteristics[3], true)*/

                    Timer().schedule(object : TimerTask() {
                        override fun run() {
                            gatt.services[2].characteristics[0].descriptors.forEach {
                                it.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                                gatt.writeDescriptor(it)
                            }

                            gatt.setCharacteristicNotification(
                                gatt.services[2].characteristics[0],
                                true
                            )

                            gatt.readCharacteristic(gatt.services[2].characteristics[0])
                        }
                    }, 200)

                    Timer().schedule(object : TimerTask() {
                        override fun run() {
                            gatt.services[2].characteristics[2].descriptors.forEach {
                                it.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                                gatt.writeDescriptor(it)
                            }

                            gatt.setCharacteristicNotification(
                                gatt.services[2].characteristics[2],
                                true
                            )

                            gatt.readCharacteristic(gatt.services[2].characteristics[2])
                        }
                    }, 400)

                    Timer().schedule(object : TimerTask() {
                        override fun run() {
                            gatt.services[2].characteristics[3].descriptors.forEach {
                                it.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                                gatt.writeDescriptor(it)
                            }

                            gatt.setCharacteristicNotification(
                                gatt.services[2].characteristics[3],
                                true
                            )

                            gatt.readCharacteristic(gatt.services[2].characteristics[3])
                        }
                    }, 600)

                    Timer().schedule(object : TimerTask() {
                        override fun run() {
                            gatt.services[2].characteristics[4].descriptors.forEach {
                                it.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                                gatt.writeDescriptor(it)
                            }

                            gatt.setCharacteristicNotification(
                                gatt.services[2].characteristics[4],
                                true
                            )

                            gatt.readCharacteristic(gatt.services[2].characteristics[4])
                        }
                    }, 800)

                    Log.d("BleNotif", "j'ai fini onServicesDiscovered'")

                    //gatt.readCharacteristic(gatt.services[2].characteristics[0])
                    gatt.readCharacteristic(gatt.services[2].characteristics[1])
                    //gatt.readCharacteristic(gatt.services[2].characteristics[2])
                    //gatt.readCharacteristic(gatt.services[2].characteristics[3])

                    super.onServicesDiscovered(gatt, status)
                }

                override fun onDescriptorRead(
                    gatt: BluetoothGatt?,
                    descriptor: BluetoothGattDescriptor?,
                    status: Int
                ) {
                    Log.d("BleNotif", "je read le descriptor")
                    super.onDescriptorRead(gatt, descriptor, status)
                    //binding.Force1.text = gatt!!.services[2].characteristics[1].value.toString()
                }

                @SuppressLint("MissingPermission")
                override fun onCharacteristicRead(
                    gatt: BluetoothGatt,
                    characteristic: BluetoothGattCharacteristic?,
                    status: Int
                ) {
                    Log.d("BleNotif", "je lis")
                    super.onCharacteristicRead(gatt, characteristic, status)
                    timer = Timer()
                    timer?.schedule(object : TimerTask() {
                        override fun run() {

                                /*gatt.services[2].characteristics[0].value?.let {
                                    val hex1 = it.joinToString("") { byte -> "%02x".format(byte) }
                                        .uppercase(Locale.FRANCE)
                                    print("")
                                    val value1 = "Valeur du capteur 1 : ${String(it)} Hex : 0x$hex1"

                                    binding.Force1.text = value1

                                }

                               /* gatt.services[2].characteristics[1].value?.let {
                                    val hex2 = it.joinToString("") { byte -> "%02x".format(byte) }
                                        .uppercase(Locale.FRANCE)
                                    print("")
                                    val value2 = "Valeur du capteur 2 : ${String(it)} Hex : 0x$hex2"

                                    binding.Force2.text = value2

                                }*/


                                // binding.ValueText2.text =
                                // gatt.services[2].characteristics[0].value.toString()

                                gatt.services[2].characteristics[2].value?.let {
                                    val hex3 = it.joinToString("") { byte -> "%02x".format(byte) }
                                        .uppercase(Locale.FRANCE)
                                    print("")
                                    val value3 = "Valeur du capteur 3 : ${String(it)} Hex : 0x$hex3"

                                    binding.Force3.text = value3

                                }

                                gatt.services[2].characteristics[3].value?.let {
                                    val hex4 = it.joinToString("") { byte -> "%02x".format(byte) }
                                        .uppercase(Locale.FRANCE)
                                    print("")
                                    val value4 = "Valeur du capteur 4 : ${String(it)} Hex : 0x$hex4"

                                    binding.Force4.text = value4

                                }*/
                        }
                    }, 0, 1000)

                }


                fun ByteArray.toHexString(): String =joinToString(separator = " ", prefix = "0x") { String.format("%02X", it) }

                override fun onCharacteristicChanged(
                    gatt: BluetoothGatt,
                    characteristic: BluetoothGattCharacteristic?
                ) {
                    super.onCharacteristicChanged(gatt, characteristic)
                    timer = Timer()
                    timer?.schedule(object : TimerTask() {
                        override fun run() {
                            runOnUiThread {

                                gatt.services[2].characteristics[0].value?.let {
                                    val hex1 = it.joinToString("") { byte -> "%02x".format(byte) }
                                        .uppercase(Locale.FRANCE)
                                    print("")
                                    val value1 = "Valeur capteur 1 : ${String(it)} $hex1"

                                    binding.Force1.text = value1
                                    val imagerouge1 = findViewById<ImageView>(R.id.capteur1)

                                    if(hex1 == "00")
                                    {
                                        comptage1 += 1
                                        binding.compteur1.text = comptage1.toString()
                                        imagerouge1.setImageResource(R.drawable.ic_red_circle)
                                    }
                                    else
                                    {
                                        imagerouge1.setImageResource(R.drawable.ic_circle_white)
                                    }



                                }

                                gatt.services[2].characteristics[1].value?.let {
                                    val hex2 = it.joinToString("") { byte -> "%02x".format(byte) }
                                        .uppercase(Locale.FRANCE)
                                    print("")
                                    val value2 = "Valeur capteur 2 : ${String(it)} $hex2"

                                    binding.Force2.text = value2
                                    val imagerouge2 = findViewById<ImageView>(R.id.capteur2)
                                   // imagerouge2.setImageResource(R.drawable.ic_red_circle)

                                    if(hex2 == "00")
                                    {
                                        comptage2 += 1
                                        binding.compteur2.text = comptage2.toString()
                                        imagerouge2.setImageResource(R.drawable.ic_red_circle)
                                    }
                                    else
                                    {
                                        imagerouge2.setImageResource(R.drawable.ic_circle_white)
                                    }
                                }



                                // binding.ValueText2.text =
                                // gatt.services[2].characteristics[0].value.toString()


                                gatt.services[2].characteristics[2].value?.let {
                                    val hex3 = it.joinToString("") { byte -> "%02x".format(byte) }
                                        .uppercase(Locale.FRANCE)
                                    print("")
                                    val value3 = "Valeur capteur 3 : ${String(it)} $hex3"

                                    binding.Force3.text = value3
                                    val imagerouge3 = findViewById<ImageView>(R.id.capteur3)
                                   // imagerouge3.setImageResource(R.drawable.ic_red_circle)

                                    if(hex3 == "00")
                                    {
                                        comptage3 += 1
                                        binding.compteur3.text = comptage3.toString()
                                        imagerouge3.setImageResource(R.drawable.ic_red_circle)
                                    }
                                    else
                                    {
                                        imagerouge3.setImageResource(R.drawable.ic_circle_white)
                                    }
                                }
                                // binding.ValueText2.text =
                                // gatt.services[2].characteristics[0].value.toString()



                                gatt.services[2].characteristics[3].value?.let {
                                    val hex4 = it.joinToString("") { byte -> "%02x".format(byte) }
                                        .uppercase(Locale.FRANCE)
                                    print("")
                                    val value4 = "Valeur capteur 4 : ${String(it)} $hex4"
                                    binding.Force4.text = value4
                                    val imagerouge4 = findViewById<ImageView>(R.id.capteur4)
                                  //  imagerouge4.setImageResource(R.drawable.ic_red_circle)

                                    if(hex4 == "00")
                                    {
                                        comptage4 += 1
                                        binding.compteur4.text = comptage4.toString()
                                        imagerouge4.setImageResource(R.drawable.ic_red_circle)
                                    }
                                    else
                                    {
                                        imagerouge4.setImageResource(R.drawable.ic_circle_white)
                                    }
                                }
                                // binding.ValueText2.text =
                                // gatt.services[2].characteristics[0].value.toString()

                                val hex5: ByteArray = gatt.services[2].characteristics[4].getValue()
                                val x = hex5.get(0).toInt()
                                val y = hex5.get(1).toInt()
                                val z = hex5.get(2).toInt()
                                binding.axex.text = x.toString()
                                binding.axey.text = y.toString()
                                binding.axez.text = z.toString()

                            }
                        }
                    }, 0, 1000)

                }
            })
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), ALL_PERMISSION_REQUEST_CODE
            )
        }
    }


    private fun askBluetoothPermission() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH_REQUEST_CODE)
        }
    }
}
