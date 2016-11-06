package com.harmazing.framework.extension.interfaces;

import com.harmazing.framework.extension.ParserContext;
import com.harmazing.framework.extension.config.Configuration;
import com.harmazing.framework.extension.config.Node;


public interface INodeParser {

	Configuration parse(Node node, ParserContext context);

}
