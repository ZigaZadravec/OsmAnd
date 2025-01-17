# OsmAnd AIDL API

## Zakaj OsmAnd AIDL API? ❓

OsmAnd AIDL API omogoča enostavno integracijo aplikacij z zmogljivimi funkcionalnostmi OsmAnd brez potrebe po obsežni kodi ali posebnih licenčnih omejitvah.
Glavni koncept temelji na komunikaciji naše aplikacije z OsmAnd aplikacijo preko AIDL ali Intents API.
To omogoča dostop do širokega nabora funkcij, kot so navigacija, pridobivanje GPS lokacije, UI elementi in drugo.

## Prednosti in slabosti

### Prednosti ✅
- **Brez licenčnih omejitev**: API je na voljo za vse namene.
- **Stalna podpora**: OsmAnd ekipa redno vzdržuje in posodablja API.
- **Enostavna integracija**: Implementacija zahteva minimalno količino kode (en razred).
- **Ločitev logike**: OsmAndova logika in poslovna logika aplikacije sta jasno ločeni.
- **Majhna velikost**: API zavzema le približno 2 MB.

### Slabosti ❌
- **Odvisnost od OsmAnd aplikacije**: OsmAnd mora biti nameščen na napravi skupaj z vašo aplikacijo.
- **Omejeno število funkcij**: V primerjavi s celotno knjižnico AIDL API metoda ponuja nekoliko manj funkcij.
- **Omejene možnosti prilagoditev**: Možnosti za prilagoditev so omejene, glede na celotno knjižnico, čeprav omogoča prilagojene logotipe, profile in stile zemljevidov.
- **Brez prilagodljivih UI elementov**: Interakcija je omejena na preklapljanje med zasloni aplikacij in povratnimi klici.

## Kaj sploh je AIDL? 🤔

Android Interface Definition Language ([AIDL](https://developer.android.com/develop/background-work/services/aidl)) omogoča definiranje programskega vmesnika, s katerim se stranka in storitev zmenita o načinu za medprocesno komunikacijo (IPC).
Na Androidu en proces običajno ne more dostopati do pomnilnika drugega procesa. Za komunikacijo morajo procesi svoje objekte razstaviti v primitive, ki jih operacijski sistem razume, in jih prenesti čez med procesi.
Pisanje kode za takšno prenašanje je zamudno, zato Android za to poskrbi s pomočjo AIDL vmesnika, ki pa za nas zgenerira potrebno kodo.
Pretvorbe objektov temeljijo na razredu `Parcelable`. Ponavadi se iz ene aplikacije kliče funkcija druge aplikacije.

## Licenca 📜

OsmAnd je licenciran pod licenco Creative Commons Attribution-NonCommercial-NoDerivs 2.0 Generic (CC BY-NC-ND 2.0) in GNU GENERAL PUBLIC LICENSE, Verzija 3 z izjemo.
[Dodatne informacije o licenci](https://osmand.net/help-online/license/).

OsmAnd AIDL API pa je odprtokoden in brezplačen za uporabo v komercialne in nekomercialne namene. Licenca zagotavlja, da lahko API vključite v katerokoli aplikacijo brez pravnih omejitev.

## Število uporabnikov

OsmAnd aplikacija ima na Google Play Store več kot 10M prenosov in slabih 200k komentarjev s povprečno oceno 4,6.

![image](https://github.com/user-attachments/assets/eb2c93db-8843-45d9-a588-8ad10fec00cf)

## Informacije o projektu

- **Število zvezdic, sledilcev in forkov**:

![image](https://github.com/user-attachments/assets/4cdd4ce0-572c-46ec-8032-cae89b0c033a)

- **Število razvijalcev**: OsmAnd ima 961 contributerjev.

![image](https://github.com/user-attachments/assets/4eeabc56-425f-4039-8fd1-3730c268df2f)

- **Zadnja posodobitev**: Pred šestimi urami od časa pisanja tega dokumenta. Na dan je v povprečju 100 commitov.
    
## Navodila za integracijo

V `AndroidManifest.xml` dodamo `queries`, da lahko `PackageManager` najde njihovo OsmAnd aplikacijo.
```
<queries>
    <package android:name="net.osmand" />
    <package android:name="net.osmand.plus" /> <!-- plačljiva različica aplikacije -->
</queries>
```

V sam projekt pa dodamo razred [OsmAndHelper.java](https://github.com/osmandapp/osmand-api-demo/blob/master/OsmAnd-api-sample/app/src/main/java/net/osmand/osmandapidemo/OsmAndHelper.java).
Že samo ta razred nam omogoča uporabo Intent API.

Če pa želimo uporabljati AIDL pa potrebujemo še ta razred [OsmAndAidlHelper.java](https://github.com/osmandapp/osmand-api-demo/blob/master/OsmAnd-api-sample/app/src/main/java/net/osmand/osmandapidemo/OsmAndAidlHelper.java).

Prav tako moramo v `build.gradle.kts`(od aplikacije) spremeniti `compileSdk` na verzijo 35.
```
android {
    namespace = "ime_paketa"
    compileSdk = 35
}
```

## Uporaba Intents API in AIDL API

Seznam Intents API funkcij je na voljo na [link](https://github.com/osmandapp/osmand-api-demo/blob/master/OsmAnd-api-sample/README.md#intent).
Seznam AIDL API funkcij: [link](https://github.com/osmandapp/osmand-api-demo/blob/master/OsmAnd-api-sample/README.md#aidl).

V željenem Activitiju ali Fragmentu osmAnd aplikacijo kličemo preko instance razreda `osmAndHelper`.
```
class MainActivity : AppCompatActivity(), OsmAndHelper.OnOsmandMissingListener {
    private lateinit var osmAndHelper: OsmAndHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        // instanciranje razreda 1001 je koda za njihov API
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

        ...
    }

    override fun osmandMissing() {
        OsmAndMissingDialogFragment().show(supportFragmentManager, null)
    }
}
```

Primer dialoga, če nimamo nameščene osmAnd aplikacije:

![image](https://github.com/user-attachments/assets/2baa0a18-07e4-4dcc-8f10-ce377100c780)

Primer odprtja zempljevida, centrirano na lokaciji FERI-ja:

![image](https://github.com/user-attachments/assets/c25eac69-0c0a-465c-9b72-4493b2fa4afd)


Primer zaznamka priljubljeno:

![image](https://github.com/user-attachments/assets/fe51e538-fdb9-466c-8bbd-b02f32ab863f)

Primer navigacije z avtom:

![image](https://github.com/user-attachments/assets/f08ba372-8f61-4f02-9f4b-cca1ce5645dc)


## Uporaba polne knjižnice

Če ne želimo, da je naša aplikacija odvisna od osmAnd aplikacije, lahko sami v svojo aplikacijo dodamo njihovo izvorno kodo. S tem nam je na voljo ves nabor funkcij.

Osebno ne priporočam tega pristopa, saj si je treba vzpostavit razvojno okolje [vzpostavitev razvojnega okolja](https://osmand.net/docs/technical/build-osmand/setup-the-dev-environment/), kar je zamuden proces.
Obenem bo zaradi tega velikost same aplikacije narasla za približno 150 MB.
[Dodatne informacije](https://creator.osmand.net/docs/technical/osmand-api-sdk/#android-osmand-full-library-sdk).

Prav tako je potrebno za to veliko več znanja o samem delovanju aplikacije.
[Primer kode](https://creator.osmand.net/docs/technical/osmand-api-sdk/simple_map_activity/)

