package pora.zadravec.osmand

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pora.zadravec.osmand.dialogs.OsmAndMissingDialogFragment

class MainActivity : AppCompatActivity(), OsmAndHelper.OnOsmandMissingListener {
    private lateinit var osmAndHelper: OsmAndHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        osmAndHelper = OsmAndHelper(this, 1001, this)

        osmAndHelper.addMapMarker(46.55920, 15.63847, "Marker Feri Primer")

        osmAndHelper.addFavorite(
            46.55937, //zempljepisna širina
            15.63792, //dolžina
            "Primer Favourite", //ime
            "tukaj še lahko dodamo opis", //opis
            "categorija", // kategorija
            "lightblue", // barva
            true // vidnost
        )

        // odpre zemljevid na specifični lokaciji, v tem primeru FERI
        osmAndHelper.showLocation(46.55909, 15.63808)

        //Vodenje od začetne lokacije pa do končne lokacije
        osmAndHelper.navigate(
            "Start Location", //ime začetne lokacije
            46.05004, // začetna zemljepisna širina
            14.50576, // začetna zemljepisna dolžina
            "Destination Location", // ime končne lokacije
            46.55909, //končna širina
            15.63808, // končna dolžina
            "car", // način prevoza
            true, // ali se naj prejšna navigacija konča z začetkom te navigacije
            true // dovoljenje za dostop do trenutne lokacije
        )


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun osmandMissing() {
        OsmAndMissingDialogFragment().show(supportFragmentManager, null)
    }
}