<!DOCTYPE html>
<html lang="hu">
<head>
  <meta charset="UTF-8">
</head>
<body>

<h1>MÁK napi bruttó vételi árfolyam betöltés</h1>

<p>
Ez a projekt automatizáltan letölti és feldolgozza a Magyar Államkincstár weboldalán elérhető
<strong>napi bruttó vételi árfolyamokat</strong>, majd feltölti az adatokat egy
<strong>PostgreSQL adatbázisba</strong>.
</p>

<h2>Előfeltételek</h2>

<ul>
  <li>Java 8+ (Tesztelve: OpenJDK 17)</li>
  <li>PostgreSQL telepítve</li>
  <li>Az adatbázisban legyen egy <code>raw</code> nevű séma a következő táblával:</li>
</ul>

<pre><code>CREATE SCHEMA IF NOT EXISTS raw;

CREATE TABLE IF NOT EXISTS raw.napi_brutto_veteli_arfolyam (
  allomany_erteknapja date NOT NULL,
  ertekpapir_megnevezese text NOT NULL,
  lejarat_datuma date NULL,
  allomany_neverteken float8 NULL,
  allomany_neverteken_deviza text NULL,
  brutto_veteli_arfolyam float8 NULL,
  allomany_brutto_veteli_arfolyamertekben float8 NULL,
  allomany_brutto_veteli_arfolyamertekben_deviza text NULL,
  CONSTRAINT napi_brutto_veteli_arfolyam_pkey PRIMARY KEY (allomany_erteknapja, ertekpapir_megnevezese)
);</code></pre>

<h2>Használat lépésről lépésre</h2>

<h3>1. PDF-ek automatikus letöltése</h3>

<h4>a) Chrome beállítása</h4>
<ol>
  <li>Nyisd meg: <code>chrome://settings/content/pdfDocuments</code></li>
  <li>Kapcsold be: <strong>"Download PDFs instead of opening them in Chrome"</strong></li>
  <li>Ha Adobe PDF reader a default PDF olvasó, akkor érdemes kikapcsolni a következőt: Preferences -> General -> Uncheck Open PDF-s automatically in Reade when they are downloaded from Chrome browser. Mert elég idegesítő, hogy folyamatosan nyitja meg.</strong></li>
</ol>

<h4>b) Webkincstár lépések</h4>
<ol>
  <li>Jelentkezz be: <strong>Befektetések → Számlakimutatások → Számlakimutatás napi bruttó vételi árfolyamon</strong></li>
  <li>Nyisd meg a fejlesztői konzolt (F12 → Console)</li>
  <li>Illeszd be a <code>src/main/java/pdfprocesser/javascript</code> fájlban található JavaScriptet</li>
  <li>Állítsd be a dátumot az 5-6. sorban</li>
  <li>A script letölti automatikusan az összes napi PDF-et</li>
</ol>

<h3>2. PDF-ek feldolgozása</h3>

<ol>
  <li>Hozz létre egy <code>pdf</code> mappát</li>
  <li>És írd át a kód tetején a te mappádra: 		String directoryPath = "C:\\DEV\\dev-workspace\\csanszi-budget\\pdf"; </li>li>
  <li>Másold ide a letöltött PDF-eket</li>
  <li>Futtasd a Java programot</li>
  <li>A program feldolgozza a PDF-eket, és feltölti az adatokat az adatbázisba</li>
</ol>

<h2>Adatbázis kapcsolat</h2>

<p>Az adatbázis jelszavát a kódban kell beállítani.</p>

<p><strong>Példa beállítás:</strong></p>
<ul>
  <li>Felhasználó: <code>postgres</code></li>
  <li>Jelszó: <code>admin</code></li>
</ul>

<h2>Könyvtárstruktúra</h2>

<pre><code>project-root/
├── pdf/                  # ide kerülnek a letöltött PDF-ek
├── src/
│   └── main/
│       └── java/
│           └── pdfprocesser/
│               └── javascript/   # itt található a letöltő JS script
└── README.md</code></pre>



</body>
</html>
