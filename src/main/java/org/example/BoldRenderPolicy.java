package org.example;

import com.deepoove.poi.policy.AbstractRenderPolicy;
import com.deepoove.poi.render.RenderContext;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;


public class BoldRenderPolicy extends AbstractRenderPolicy<String> {
    @Override
    public void doRender(RenderContext<String> context) throws Exception {
        // 正确的获取段落方式
        XWPFParagraph paragraph = (XWPFParagraph) context.getRun().getParent();

        // 清除占位符文本
        this.clearPlaceholder(paragraph, context.getEleTemplate().getSource());

        // 创建加粗的运行对象
        XWPFRun run = paragraph.createRun();
        run.setText(context.getData());
        run.setBold(true);
        run.setFontSize(14);
        this.clearPlaceholder(paragraph, " ");
    }
    private void clearPlaceholder(XWPFParagraph paragraph, String placeholder) {
        // 清除段落中的占位符文本
        paragraph.getRuns().forEach(run -> {
            String text = run.getText(0);
            if (text != null && text.contains(placeholder)) {
                run.setText(text.replace(placeholder, ""), 0);
            }
        });

    }
}