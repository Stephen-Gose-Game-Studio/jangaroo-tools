Ext.define("package1.ConfigClass", function(ConfigClass) {/*package package1{
import ext.mixin.Observable;

[Event(name="click", type="package1.someOtherPackage.SomeEvent")]

public class ConfigClass extends Observable {

  public*/ function ConfigClass$(config/*:ConfigClass = null*/) {ext.mixin.Observable.prototype.constructor.call(this);if(arguments.length<=0)config=null;
  }/*

  public var foo:String = "foo";

  public var number:int;

  public native function get items():Array;

  [DefaultProperty]
  [ExtConfig(create)]
  public native function set items(value:Array):void;

  public native function get defaults():*;

  [ExtConfig(extractXType="defaultType")]
  public native function set defaults(value:*):void;

  private var _title:String = "- empty -";

  [Bindable]
  public*/ function get$title()/*:String*/ {
    return this._title$3;
  }/*

  [Bindable]
  public*/ function set$title(value/*:String*/)/*:void*/ {
    this._title$3 = value;
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "ext.mixin.Observable",
      metadata: {
        "": [
          "Event",
          [
            "name",
            "click",
            "type",
            "package1.someOtherPackage.SomeEvent"
          ]
        ],
        items: ["DefaultProperty"]
      },
      constructor: ConfigClass$,
      foo: "foo",
      number: 0,
      _title$3: "- empty -",
      getTitle: get$title,
      setTitle: set$title,
      config: {title: undefined},
      requires: ["ext.mixin.Observable"]
    };
});