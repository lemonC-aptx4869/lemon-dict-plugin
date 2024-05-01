package cn.lemon.dict.plugin.model;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class DictConfig {

    List<DictConfigNode> dictConfigs;
}
