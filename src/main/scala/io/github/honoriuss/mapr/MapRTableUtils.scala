package io.github.honoriuss.mapr

import com.mapr.db.MapRDB
import org.apache.spark.sql.SparkSession

import scala.sys.process._

object MapRTableUtils {
  def createMapRDBTable(appName: String, host: String, tablePath: String, readPermission: String, writePermission: String): Unit = {
    val spark = SparkSession.builder()
      .appName(appName)
      .master(host)
      .getOrCreate()

    if (!MapRDB.tableExists(tablePath)) {
      // Wenn die Tabelle nicht existiert, erstelle sie
      MapRDB.createTable(tablePath)
      println("Neue MapR-DB Tabelle erstellt.")
    }

    // Setze Berechtigungen
    /*val tablePermissions = MapRDBTableOptions()
      .setAccessControl(true)  // Aktiviere Zugriffskontrolle
      .setReadAce(readPermission)         // Erlaube Lesezugriff für alle (entsprechend anpassen)
      .setWriteAce(writePermission)        // Erlaube Schreibzugriff für alle (entsprechend anpassen)
    // Weitere Berechtigungen nach Bedarf hinzufügen

    MapRDB.setTableAce(tablePath, tablePermissions)
    println("Berechtigungen für die MapR-DB Tabelle aktualisiert.")*/

    spark.stop()
  }

  def createMapRDBTableWithSSH(remoteHost: String, remoteUser: String, tableName: String, readPermission: String, writePermission: String): Unit = {

    val remotePassword = "remote_password" // Beispiel: Passwort für die SSH-Verbindung

    // SSH-Befehl zum Ausführen des MapR CLI-Befehls auf dem entfernten Rechner
    val sshpassCmd = Seq("sshpass", "-p", remotePassword, "ssh", s"$remoteUser@$remoteHost", s"maprcli table info -path $tableName")

    // Befehl auf der Befehlszeile ausführen und das Ergebnis abrufen
    val tableInfoResult = sshpassCmd.!!

    // Überprüfe, ob die Tabelle existiert
    val tableExists = tableInfoResult.contains("does not exist")

    if (!tableExists) {
      // SSH-Befehl zum Erstellen der Tabelle und Festlegen der Berechtigungen
      val sshCreateTableCmd = s"ssh $remoteUser@$remoteHost 'maprcli table create -path $tableName -readAce $readPermission -writeAce $writePermission'"

      // Befehl auf der Befehlszeile ausführen
      val createTableResult = sshCreateTableCmd.!

      if (createTableResult == 0) {
        println("MapR-DB Tabelle erfolgreich erstellt und Berechtigungen gesetzt.")
      } else {
        println("Fehler beim Erstellen der MapR-DB Tabelle oder Setzen der Berechtigungen.")
      }
    } else {
      println("Die MapR-DB Tabelle existiert bereits.")
    }
  }
}
