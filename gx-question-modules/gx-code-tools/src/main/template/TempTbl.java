package ${modelPackage};

import com.alibaba.fastjson.JSON;
import java.io.*;

public class ${className} implements Serializable {
	
#foreach($item in $!{columnDatas})
	protected $item.dataType $item.fieldName; 		/*$item.columnComment*/
#end

#if ($!{pk.keyFieldName} != "id")
	/*为了兼容主键不是ID时，create方法自动生成主键注入*/
	public void setId(String $pk.keyFieldName) {
		this.$pk.keyFieldName = $pk.keyFieldName;
	}
	public String getId() {
		return $pk.keyFieldName;
	}	
#end
#foreach($item in $!{columnDatas})
	public void set${item.maxFieldName}($item.dataType $item.fieldName){
		this.$item.fieldName = $item.fieldName;
	}
	/**$item.columnComment*/
	public $item.dataType get${item.maxFieldName}(){
		return this.$item.fieldName;
	}
#end
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
