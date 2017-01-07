(ns tic-tac-toe.util-test
  (:require [clojure.test :refer :all]
            [tic-tac-toe.util :refer :all]))

(deftest single-test
  (testing "Extracts the single item from a 1-long collection."
    (is (= :a (single [:a]))))
  (testing "Returns nil for a nil collection."
    (is (= nil (single nil))))
  (testing "Returns nil for an empty collection."
    (is (= nil (single []))))
  (testing "Returns nil for a >1-long collection."
    (is (= nil (single [:a :b]))))
  (testing "Throws IllegalArgumentException for anything besides a collection."
    (is (thrown? IllegalArgumentException (single :a)))))

(deftest to-digit-test
  (testing "Parses 0."
    (is (= 0 (to-digit \0))))
  (testing "Parses 9."
    (is (= 9 (to-digit \9))))
  (testing "Returns nil for characters below 0."
    (is (= nil (to-digit \.))))
  (testing "Returns nil for characters above 9."
    (is (= nil (to-digit \A))))
  (testing "Throws IllegalArgumentException for anything besides a char."
    (is (thrown? IllegalArgumentException (to-digit "0")))))

(deftest map-deep-test
  (testing "Same as map at depth 1."
    (is (= (map inc [1 2 3])
           (map-deep 1 inc
                     [1 2 3])) "With one collection.")
    (is (= (map + [1 2 3] [4 5 6])
           (map-deep 1 +
                     [1 2 3]
                     [4 5 6])) "With two collections."))
  (testing "Maps elements' elements at depth 2."
    (is (= [[2 3 4] [5 6 7]]
           (map-deep 2 inc
                     [[1 2 3] [4 5 6]])) "With one matrix.")
    (is (= [[8 10 12] [14 16 18]]
           (map-deep 2 +
                     [[1 2 3] [ 4  5  6]]
                     [[7 8 9] [10 11 12]])) "With two matrices."))
  (testing "Returns nil at depth <1."
    (is (= nil
           (map-deep 0 inc [1 2 3])))
    (is (= nil
           (map-deep -1 inc [1 2 3])))))

(deftest map-many-test
  (testing "Applies map over nested collections."
    (is (= ["147" "258" "369"]
           (map-many #(apply str %) [[1 2 3]
                                     [4 5 6]
                                     [7 8 9]])))))

(deftest consensus-test
  (testing "Returns first item if all equal."
    (is (= :a (consensus [:a :a :a]))))
  (testing "Returns item if only one."
    (is (= :a (consensus [:a]))))
  (testing "Returns nil if items are different."
    (is (= nil (consensus [:a :a :b])))
    (is (= nil (consensus [:a :b :b])))
    (is (= nil (consensus [:a :b]))))
  (testing "Returns nil if some items are nil."
    (is (= nil (consensus [:a :a nil])))
    (is (= nil (consensus [nil :a :a]))))
  (testing "Returns nil if all items are nil."
    (is (= nil (consensus [nil nil nil]))))
  (testing "Returns nil if no items."
    (is (= nil (consensus []))))
  (testing "Returns nil if nil list."
    (is (= nil (consensus nil)))))

(deftest all-vectors-test
  (is (= [[1 5 9]
          [1 2 3]
          [4 5 6]
          [7 8 9]
          [7 5 3]
          [7 4 1]
          [8 5 2]
          [9 6 3]]
         (all-vectors [[1 2 3]
                       [4 5 6]
                       [7 8 9]]))))

(deftest rotate-180-test
  (is (= [[9 8 7]
          [6 5 4]
          [3 2 1]]
         (rotate-180 [[1 2 3]
                      [4 5 6]
                      [7 8 9]]))))
