(ns main.hello
  (:gen-class)
  (:import (org.example.api Calculation)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
  (println (Calculation/add 1 2))
  )
