(ns tic-tac-toe.core
  (:gen-class))

(def squares {})

(defn show-board [squares]
  (printf " %s | %s | %s %n" (get squares "1" " ") (get squares "2" " ") (get squares "3" " "))
  (println "---+---+---")
  (printf " %s | %s | %s %n" (get squares "4" " ") (get squares "5" " ") (get squares "6" " "))
  (println "---+---+---")
  (printf " %s | %s | %s %n" (get squares "7" " ") (get squares "8" " ") (get squares "9" " ")))

(defn show-board-positions []
  (println "\nSelect 1 thorugh 9 from the game board")
  (show-board (zipmap (for [n (range 1 10)] (str n)) (range 1 10))))

(defn calculate-winner []
  (let [lines [["1", "2", "3"],
               ["4", "5", "6"],
               ["7", "8", "9"],
               ["1", "4", "7"],
               ["2", "5", "8"],
               ["3", "6", "9"],
               ["1", "5", "9"],
               ["3", "5", "7"]]]
    (first
      (filter string?
        (for [[a b c] lines]
          (if (and (not (nil? (get squares a)))
                   (= (get squares a) (get squares b))
                   (= (get squares a) (get squares c)))
            (get squares a)
            nil))))))

(defn valid-entry? [value]
  (let [from-1-to-9 (contains? (set (for [n (range 1 10)] (str n))) value)
        free-square (nil? (get squares value))]
    (if (not from-1-to-9)
      (println "The value entered must be a number from 1 to 9. Try again."))
    (if (not free-square)
      (println "The selected position is occupied. Try again."))
    (and from-1-to-9 free-square)))

(defn read-movement [player]
  (let [input
        (do (print (format "%n%s Turn: ", player))
            (flush)
            (read-line))]
    (if (valid-entry? input) input nil)))

(defn -main [& args]
  (loop [game-over? false x-is-next? true]
    (when (not game-over?)
      (show-board-positions)
      (let [position (read-movement (if x-is-next? "X" "O"))]
        (if (not (nil? position))
          (def squares (assoc squares position (if x-is-next? "X" "O"))))
        (show-board squares)
        (let [winner (calculate-winner)]
          (if (nil? winner)
            (if (= (count squares) 9)
              (do (println "\n*** XO DRAW! ***")
                  (recur true nil))
              (if (nil? position)
                (recur false x-is-next?)
                (recur false (not x-is-next?))))
            (do (printf "\n*** %s WINNER! ***%n" winner)
                (recur true nil))))))))
