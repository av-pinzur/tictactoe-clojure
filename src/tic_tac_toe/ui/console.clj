(ns tic-tac-toe.ui.console
  (:gen-class)
  (:use [tic-tac-toe.core]))

(defn -main
  "Implementing the classic children's game using Clojure."
  [& args]
  (println "Welcome to Av's Clojure Tic-Tac-Toe!")

  (println (sort [(->CellId \A 2) (->CellId \B 1) (->CellId \A 1)]))

  (println (str (->CellId \C 3))))
