(deftype IncludesPrivateKey
    []
    AnEnumeration
    (get-value [this match-seq]
        (let [first-match (first match-seq)]
            (if (nil? first-match)
                :eof
                (case (first (Character/toChars first-match))
                    \v :private
                    \b :public
        			:unknown)))))