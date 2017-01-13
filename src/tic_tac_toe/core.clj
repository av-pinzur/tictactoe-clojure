(ns tic-tac-toe.core
  (:require [clojure.string :as str])
  (:use [tic-tac-toe.util])
  (:import (java.time LocalDateTime Duration)))

(def row-ids [\A \B \C])
(def col-ids [1 2 3])

(defn is-valid-row-id [char]
  (some #{char} row-ids))

(defn is-valid-col-id [char]
  (some #{char} col-ids))

(defn parse-row-id [#^String raw]
  (when raw
    (let [upper (.toUpperCase raw)
          chars (seq upper)]
      (single-match is-valid-row-id chars))))

(defn parse-col-id [#^String raw]
  (when raw
    (let [chars (seq raw)
          digits (map to-digit chars)]
      (single-match is-valid-col-id digits))))

(defrecord CellId [#^char row, #^int col]
  Object
  (toString [_] (str row col))

  Comparable
  (compareTo [_ other]
    (let [row-result (compare row (:row other))]
      (if-not (zero? row-result)
        row-result
        (compare col (:col other))))))

(def cell-ids (map
                (fn [row] (map
                            #(->CellId row %)
                            col-ids))
                row-ids))

(defn parse-cell-id [#^String raw]
  (when-let [row (parse-row-id raw)]
    (when-let [col (parse-col-id raw)]
      (->CellId row col))))

(defrecord Play [#^CellId cell, #^Duration think-time])

(defrecord Game [raw-play-history])

(defn player-by-play-index [i]
  (if (even? i) :x :o))

(defn play-history [#^Game game]
  (map-indexed #(assoc %2 :player (player-by-play-index %1))
               (:raw-play-history game)))

(defn player-at [#^Game game, #^CellId cell]
  (:player (single-match
             #(= cell (:cell %))
             (play-history game))))

(defn board [#^Game game]
  (map-deep 2
            #(player-at game %)
            cell-ids))

(defn assoc-cell-ids [board]
  (map-deep 2
            (fn [cell-id player] {:cell cell-id :player player})
            cell-ids
            board))

(defn dissoc-cell-ids [board-with-cell-ids]
  (map-deep 2
            :player
            board-with-cell-ids))

(defn test-win [vector]
  (if-let [winner (consensus (map :player vector))]
    {:winner        winner
     :winning-cells (map :cell vector)}))

(defn find-win [board]
  (->> (assoc-cell-ids board)
       (all-vectors)
       (map test-win)
       (first-match some?)))

(defrecord won [player cell-ids])
(defrecord drawn [])
(defrecord ready [next-player])

(defn status [#^Game game]
  (let [board (board game)
        win (find-win board)]
    (cond
      win (->won (:player win) (:winning-cells win))
      (some #(some some? %) board) (->ready (player-by-play-index (:raw-play-history game)))
      :else (->drawn))))