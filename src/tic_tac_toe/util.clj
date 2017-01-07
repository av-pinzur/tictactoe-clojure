(ns tic-tac-toe.util
  (:import (java.lang Character)))

(defn
  ^{:arglists '([coll])
    :doc      "Returns the item in a single-item collection.
          Calls seq on its argument.
          If coll is nil, empty, or contains > 1 items, returns nil."}
  single [coll]
  (let [seq (seq coll)]
    (when (= 1 (count seq))
      (first seq))))

(defn single-match [pred coll] (single (filter pred coll)))
(defn first-match [pred coll] (first (filter pred coll)))
(defn second-match [pred coll] (second (filter pred coll)))
(defn nth-match [pred coll index] (nth (filter pred coll) index))
(defn last-match [pred coll] (last (filter pred coll)))

(defn to-digit [char]
  (when (Character/isDigit char)
    (-
      (int char)
      (int \0))))

(defn map-deep
  ([depth f coll]
   (cond
     (< depth 1) nil
     (= depth 1) (map f coll)
     :else (map #(map-deep (dec depth) f %) coll)))
  ([depth f c1 c2]
   (cond
     (< depth 1) nil
     (= depth 1) (map f c1 c2)
     :else (map #(map-deep (dec depth) f %1 %2) c1 c2))))

(defn map-many [f colls]
  (apply map (fn [& args] (f args)) colls))

(defn consensus [coll]
  (reduce
    #(if (= %1 %2) %1 (reduced nil))
    (first coll)
    (rest coll)))

(defn rotate-90 [matrix]
  (map-many reverse matrix))

(defn rotate-180 [matrix]
  (rotate-90 (rotate-90 matrix)))

(defn horizontal-vectors [matrix]
  matrix)

(defn downhill-vector [matrix]
  (map-indexed #(nth %2 %1) matrix))

(defn horizontal-and-downhill-vectors [matrix]
  (cons (downhill-vector matrix)
        (horizontal-vectors matrix)))

(defn all-vectors [matrix]
  (concat (horizontal-and-downhill-vectors matrix)
          (horizontal-and-downhill-vectors (rotate-90 matrix))))
