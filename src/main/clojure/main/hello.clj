(ns main.hello
  (:gen-class)
  (:require [wrapper.twitter :refer [add]]))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
  (def result (add 342 939))
  (println result)
  )
