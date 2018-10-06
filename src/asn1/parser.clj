(ns asn1.parser
  (:gen-class)
  (:require [clojure.string  :as str]
            [clojure.java.io :as io]
            [clojure.pprint  :refer [pprint]])
  (:import (java.io RandomAccessFile)
           (java.nio ByteBuffer)
           (javax.xml.bind DatatypeConverter)
           (org.bouncycastle.asn1 ASN1InputStream)
           (org.bouncycastle.asn1.util ASN1Dump)))

(defn base64-extract
  [path]
  (reduce str "" (remove #(str/starts-with? % "----") (line-seq (io/reader path)))))

(defn base64-bytes
  [path]
  (let [b64-str ^String (base64-extract path)]
    (DatatypeConverter/parseBase64Binary b64-str)))

(defn base64-buffer
  [path]
  (ByteBuffer/wrap (base64-bytes path)))

(defn parse-asn1
  [bb]
  ;; Not really sure.
  bb)

(defn cheat
  [path]
  (with-open [ais (ASN1InputStream. (io/input-stream path))]
    (while (pos? (.available ais))
      (let [o (.readObject ais)
            s (ASN1Dump/dumpAsString o true)]
        (println s)))))

(defn -main
  [& args]
  (if-let [key-path (first args)]
    (pprint (parse-asn1 (base64-buffer key-path)))
    (binding [*out* *err*]
      (println "no path given")
      (System/exit 1))))
