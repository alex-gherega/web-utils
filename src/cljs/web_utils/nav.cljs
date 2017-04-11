(ns web-utils.nav)

(defn- build-style [tag-kwd style-kwds]
  (keyword (reduce str (name tag-kwd) (map #(str "." (name %)) style-kwds))))

(defn- build-nav-entry [link-name link & style]
  (let [data {:href link :key (hash link-name)}]
    (conj (vec style) data link-name)))

(defn build-nav [links-map & style-kwds]
  "Builds a sequence of HTML elements. links-map is a map where
   the keys are the keyword of a page/section name and values
   are the corresponding links (e.g. {:home \"/index.html\"}).
   style-kwds are the style class name(s) as keyword(s) (e.g. :css-class)."
  (let [style (build-style :a style-kwds)]
    (doall
     (map #(build-nav-entry
            (-> % first name clojure.string/capitalize)
            (-> % second)
            style) links-map))))
