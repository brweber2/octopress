(deftype EnumeratedValue
    [the-length an-enumeration]
    SectionInfo
    (get-section [this stream-seq parsed-so-far]
        (let [the-seq (take the-length stream-seq)]
            (get-value an-enumeration the-seq))))