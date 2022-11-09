package com.example.liquidmixerv2

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var m_bluetoothAdapter: BluetoothAdapter? = null
    lateinit var m_pairedDevices: Set<BluetoothDevice>
    val REQUEST_ENABLE_BLUETOOTH = 1

    companion object{ //singleton
        val EXTRA_ADDRESS: String = "Device_address"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter() //cihazdaki ble adaptorunu buluyor
        if(m_bluetoothAdapter == null){
1
            Toast.makeText(
                applicationContext,
                "This device does not support bluetooth",
                Toast.LENGTH_LONG);
            return
        }
        if(!m_bluetoothAdapter!!.isEnabled){ //!!kesinlikle null olmadığını gösteriyor
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)

            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
        }

        select_device_refresh.setOnClickListener{ pairedDeviceList() }

    }
    private fun pairedDeviceList(){ //galeriye git foto al gel result ı olan bir activity e gecis


        m_pairedDevices = m_bluetoothAdapter!!.bondedDevices //pair edilenleri görüyor yani pair edeceksin ondan sonra geleceksin
        val list:ArrayList<BluetoothDevice> = ArrayList()

        if(!m_pairedDevices.isEmpty()){
            for(device: BluetoothDevice in m_pairedDevices){
                list.add(device)
                Log.i("device", ""+device)
            }
        }else{
            Toast.makeText(
                applicationContext,
                "No paired Bluetooth device is found",
                Toast.LENGTH_LONG);
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        select_device_list.adapter = adapter
        select_device_list.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val device: BluetoothDevice = list[position]
            val address: String = device.address

            val intent = Intent(this, COntrolActivityy::class.java)
            intent.putExtra(EXTRA_ADDRESS, address)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_ENABLE_BLUETOOTH){
            if(resultCode == Activity.RESULT_OK){
                if(m_bluetoothAdapter!!.isEnabled){

                    Toast.makeText(
                        applicationContext,
                        "Bluetooth has been enabled.",
                        Toast.LENGTH_LONG);
                }else{
                    Toast.makeText(
                        applicationContext,
                        "Bluetooth has been disabled.",
                        Toast.LENGTH_LONG);
                }
            }else if(resultCode == Activity.RESULT_CANCELED){

                Toast.makeText(
                    applicationContext,
                    "Bluetooth enabling has been canceled.",
                    Toast.LENGTH_LONG);
            }
        }
    }
}