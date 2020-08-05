(ns bootstrap.3.elements
  (:require [hiccup.custom-elements :refer [render-element]]))

(defmethod render-element ::glyphicon [_ _ [glyph]]
  [:span {:class {:glyphicon true
                  [:glyphicon glyph] glyph}
          :aria-hidden true}])

(defmethod render-element ::container [_ {:keys [fluid?] :as attrs} content]
  [:div (-> attrs
            (update :class merge {[:container (when fluid? :fluid)] true}))
   content])

(defmethod render-element ::jumbotron [_ attrs body]
  [:div.jumbotron attrs body])

(defmethod render-element ::button
  [_ {:keys [variant size]
      :or {variant :primary}
      :as attrs}
   body]
  [:button {:class {:btn true
                    [:btn variant] true
                    [:btn size] size}}
   body])

(defmethod render-element ::panel [_
                                   {:keys [title footer header post-body
                                           bs-style
                                           collapsible? expanded?]
                                    :or {bs-style :default
                                         title "title"
                                         heading "heading"
                                         footer "footer"}
                                    :as attrs}
                                   body]
  [(if collapsible? :details :div)
   {:class {:panel true
            [:panel bs-style] bs-style}
    :open expanded?}
   (when (or title header)
     [(if collapsible? :summary :div) {:class :panel-heading}
      (if title [:span.panel-title " " title] header)])
   (some->> (or body)
            (conj [:div.panel-body]))
   post-body
   (some->> footer (conj [:div.panel-footer]))])

(defmethod render-element ::navbar
  [_ {:keys [bs-style brand brand-link component-element
             fluid? position fixed static]
      :or {bs-style :default
           component-element :nav
           brand-link "#"
           brand "Brand"}}
   content]
  [component-element
   {:class {:navbar true
            [:navbar bs-style] bs-style
            [:navbar :fixed fixed] fixed
            [:navbar :static static] static}}
   [::container
    {:fluid? fluid?}
    [:div.navbar-header
     [:button {:type :button
               :class {:navbar-toggle true
                       :collapsed true}
               :data-toggle :collapse
               :data-target "#navbar-collapse1"
               :aria-expanded "false"}
      [:span.sr-only "Toggle navigation"]
      (repeat 3 [:span.icon-bar])]
     [:a.navbar-brand {:href brand-link} brand]]
    [:div.navbar-collapse.collapse
     {:id "navbar-collapse1"}
     content]]])

(defmethod render-element ::featurette
  [_ {:keys [heading secondary img]
      :or {secondary "secondary" img "?"}}
   text]
  [:div.row.featurette
   [:div.col-md-7
    [:h2.featurette-heading
     (or heading "First featurette heading. ")
     " "
     [:span.text-muted secondary]]
    [:p.lead
     (or text "Donec ullamcorper nulla non metus auctor fringilla. Vestibulum id ligula porta felis euismod semper. Praesent commodo cursus magna, vel scelerisque nisl consectetur. Fusce dapibus, tellus ac cursus commodo.")]]
   [:div.col-md-5
    [:img.featurette-image.img-responsive.center-block
     {:height 500
      :width 500
      :alt "500x500",
      :src img}]]])

(defmethod render-element ::basic-layout
  [_ {:keys [language-code title extra-head extra-script
             jquery-url base-url rtl?]
      :or {base-url "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7"
           jquery-url "https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"
           title "Bootstrap template title"}}
   content]
  (list
    "<!DOCTYPE html>"
    [:html {:lang language-code}
     [:head
      [:meta {:charset :utf-8}]
      [:meta {:content "IE=edge"
              :http-equiv "X-UA-Compatible"}]
      [:meta {:name "viewport"
              :content "width=device-width, initial-scale=1"}]
      [:title title]
      #_[:link {:href (str base-url "/css/bootstrap.min.css")
                :rel :stylesheet}]
      (when rtl?
        [:link {:href "https://cdn.rawgit.com/morteza/bootstrap-rtl/v3.3.4/dist/css/bootstrap-rtl.min.css"
                :rel :stylesheet}])
      "<!--[if lt IE 9]>"
      [:script {:src "https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"}]
      [:script {:src "https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"}]
      "<![endif]-->"
      extra-head]
     [:body
      (or content "Bootstrap body content")
      [:script {:src jquery-url}]
      [:script {:src (str base-url "/js/bootstrap.min.js")}]
      extra-script]]))


