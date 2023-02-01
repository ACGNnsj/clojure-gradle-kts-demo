(ns main.hello
  (:gen-class)
  (:require [wrapper.twitter :refer [add]]))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
  (println (add 4 5))
  )
