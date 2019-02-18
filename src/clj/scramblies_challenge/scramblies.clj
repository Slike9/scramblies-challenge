(ns scramblies-challenge.scramblies)

(defn scramble?
  "Returns true if a portion of str1 characters can be rearranged to match str2, otherwise returns false"
  [str1 str2]
  (let [str1-char-freqs (frequencies str1)
        str2-char-freqs (frequencies str2)]
    (every? (fn [[chr freq2]] (>= (str1-char-freqs chr 0) freq2)) str2-char-freqs)))
