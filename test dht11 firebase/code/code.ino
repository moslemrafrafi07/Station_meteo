#include <WiFi.h>
#include <FirebaseESP32.h>
#include <DHT.h>

#define WIFI_SSID "Airbox-EB4D"
#define WIFI_PASSWORD "NC9wTdZGZQAf"
#define FIREBASE_HOST "https://esp32-fb-cea18-default-rtdb.firebaseio.com/"
#define FIREBASE_AUTH "AIzaSyB2nLvPgG-hjZZNUkyOPRRPF988GggTDtA"

#define DHTPIN 16
#define DHTTYPE DHT11

DHT dht(DHTPIN, DHTTYPE);

FirebaseData fbdo;

void setup() {
  Serial.begin(115200);

  // Connexion au réseau WiFi
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("Connexion au WiFi en cours...");
  }
  Serial.println("Connecté au WiFi");

  // Initialisez la connexion à Firebase
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);

  dht.begin();
}

void loop() {
  float humidity = dht.readHumidity();
  float temperature = dht.readTemperature();

  if (isnan(humidity) || isnan(temperature)) {
    Serial.println("Échec de la lecture du capteur DHT11.");
    return;
  }

  // Ajoute "C" après la température
  String temperatureWithUnit = String(temperature) + " C";

  // Ajoute "%" après l'humidité
  String humidityWithUnit = String(humidity) + "%";

  // Enregistrez les données dans Firebase avec les unités
  Firebase.setString(fbdo, "/dht11/temperature", temperatureWithUnit);
  Firebase.setString(fbdo, "/dht11/humidity", humidityWithUnit);

  if (Firebase.setString(fbdo, "/dht11/temperature", temperatureWithUnit) && Firebase.setString(fbdo, "/dht11/humidity", humidityWithUnit)) {
    Serial.println("Données mises à jour avec succès dans Firebase !");
  } else {
    Serial.println("Échec de la mise à jour des données dans Firebase.");
  }

  delay(1500); // Envoyer les données toutes les 60 secondes
}
