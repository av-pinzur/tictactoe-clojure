(ns tic-tac-toe.core-test
  (:require [clojure.test :refer :all]
            [tic-tac-toe.core :refer :all])
  (:import (java.lang Object)
           (java.time Duration)))

(deftest parse-row-test
  (testing "Parses bare format."
    (is (= \B (parse-row-id "B"))))
  (testing "Parses from cell id format."
    (is (= \B (parse-row-id "B2"))))
  (testing "Parses lowercase."
    (is (= \B (parse-row-id "b2"))))
  (testing "Returns nil if no valid row found."
    (is (= nil (parse-row-id "z"))))
  (testing "Returns nil for empty string."
    (is (= nil (parse-row-id ""))))
  (testing "Returns nil for nil."
    (is (= nil (parse-row-id nil)))))

(deftest parse-col-test
  (testing "Parses bare format."
    (is (= 2 (parse-col-id "2"))))
  (testing "Parses from cell id format."
    (is (= 2 (parse-col-id "B2"))))
  (testing "Returns nil for empty string."
    (is (= nil (parse-row-id ""))))
  (testing "Returns nil for nil."
    (is (= nil (parse-row-id nil)))))

(deftest cell-ids-test
  (testing "Contains correct elements in correct order."
    (is (= [[(->CellId \A 1) (->CellId \A 2) (->CellId \A 3)]
            [(->CellId \B 1) (->CellId \B 2) (->CellId \B 3)]
            [(->CellId \C 1) (->CellId \C 2) (->CellId \C 3)]]
           cell-ids))))

(deftest parse-cell-id-test
  (testing "Parses standard format."
    (is (= (->CellId \B 2) (parse-cell-id "B2"))))
  (testing "Parses lowercase."
    (is (= (->CellId \B 2) (parse-cell-id "b2"))))
  (testing "Parses despite noise."
    (is (= (->CellId \B 2) (parse-cell-id " sf98pO*&^*&^OJLNb SDFD*&^9872"))))
  (testing "Returns nil for nil."
    (is (= nil (parse-cell-id nil))))
  (testing "Returns nil for empty."
    (is (= nil (parse-cell-id "nil"))))
  (testing "Returns nil for garbage."
    (is (= nil (parse-cell-id " sf98pO*&^*&^OJLN SDFD*&^987"))))
  (testing "Returns nil for row only."
    (is (= nil (parse-cell-id " sf98pO*&^*&^OJLNb SDFD*&^987"))))
  (testing "Returns nil for col only."
    (is (= nil (parse-cell-id " sf98pO*&^*&^OJLN SDFD*&^9872")))))

(deftest player-by-play-index-test
  (testing "X goes first."
    (is (= :x (player-by-play-index 0))))
  (testing "O goes second."
    (is (= :o (player-by-play-index 1))))
  (testing "Then they take turns."
    (is (= :x (player-by-play-index 2)))
    (is (= :o (player-by-play-index 3)))))

(def game (->Game [
                   (->Play (->CellId \A 1) (Duration/ofSeconds 5)),
                   (->Play (->CellId \B 2) (Duration/ofSeconds 5))]))

(deftest play-history-test
  (testing "2nd play is O."
    (is (= :o
           (:player (second (play-history game)))))))

(deftest player-at-test
  (testing "O is on B2."
    (is (= :o
           (player-at game (->CellId \B 2))))))

(deftest board-test
  (testing "Board is 3x3 square with player ids where appropriate."
    (is (= [(list :x nil nil)
            (list nil :o nil)
            (list nil nil nil)]
           (board game)))))

(deftest assoc-cell-ids-test
  (testing "Turns a matrix of (possibly nil) player ids into cell, player pairs"
    (is (= [[{:cell (->CellId \A 1) :player nil}
             {:cell (->CellId \A 2) :player nil}
             {:cell (->CellId \A 3) :player :x}],
            [{:cell (->CellId \B 1) :player :o}
             {:cell (->CellId \B 2) :player :o}
             {:cell (->CellId \B 3) :player :o}],
            [{:cell (->CellId \C 1) :player :x}
             {:cell (->CellId \C 2) :player :x}
             {:cell (->CellId \C 3) :player nil}]]
           (assoc-cell-ids [[nil nil  :x]
                            [ :o  :o  :o]
                            [ :x  :x nil]])))))

(deftest test-win-test
  (testing "Returns a player id and a vector of cell ids for a win."
    (is (= {:winner :o
            :winning-cells [(->CellId \B 1) (->CellId \B 2) (->CellId \B 3)]}
           (test-win [{:cell (->CellId \B 1) :player :o}
                      {:cell (->CellId \B 2) :player :o}
                      {:cell (->CellId \B 3) :player :o}]))))
  (testing "Returns nil if no win."
    (is (= nil
           (test-win [{:cell (->CellId \C 1) :player :x}
                      {:cell (->CellId \C 2) :player :x}
                      {:cell (->CellId \C 3) :player nil}])))))

(deftest find-win-test
  (testing "Horizontal win is detected."
    (is (= {:winner        :o
            :winning-cells [(->CellId \B 1)
                            (->CellId \B 2)
                            (->CellId \B 3)]}
           (find-win [[nil nil :x]
                      [:o :o :o]
                      [:x :x nil]]))))
  (testing "Vertical win is detected."
    (is (= {:winner :o :winning-cells [(->CellId \C 3)
                                       (->CellId \B 3)
                                       (->CellId \A 3)]}
           (find-win [[nil nil :o]
                      [nil :x  :o]
                      [ :x :x  :o]]))))
  (testing "Diagonal win is detected."
    (is (= {:winner :o :winning-cells [(->CellId \C 1)
                                       (->CellId \B 2)
                                       (->CellId \A 3)]}
           (find-win [[:x nil :o]
                      [:x :o  nil]
                      [:o :x  nil]]))))
  (testing "Returns nil if no win detected."
    (is (= nil
           (find-win [[nil nil :x]
                      [:o  :o  nil]
                      [:x  :x  nil]])))))