package ext.config {

[ExtConfig(target="ext.Panel", xtype="panel")]
public class panel extends component{

  /**
   * List of child items
   */
  public native function get items():Array;
  /**
   * @private
   */
  public native function set items(value:Array):void;

  /**
   * List of tools
   */
  public native function get tools():Array;
  /**
   * @private
   */
  public native function set tools(value:Array):void;

   /**
   * List of child items
   */
  public native function get menu():Array;
  /**
   * @private
   */
  public native function set menu(value:Array):void;

  /**
   * TODO: This should come from component!
   * the x value
   */
  public native function get x():Number;
  /**
   * @private
   */
  public native function set x(value:Number):void;

  /**
   * visible
   */
  public native function get visible():Boolean;
  /**
   * @private
   */
  public native function set visible(value:Boolean):void;
}
}
