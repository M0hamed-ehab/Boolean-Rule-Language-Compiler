package ast.visualization;

import ast.AstNode;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public final class AstVisualizer {
    private static final AtomicBoolean PLATFORM_READY = new AtomicBoolean(false);
    private static final Object PLATFORM_LOCK = new Object();

    private AstVisualizer() {
    }

    public static void show(AstNode root) {
        show(root, "AST Visualizer");
    }

    public static void show(AstNode root, String title) {
        showInternal(root, title, false);
    }

    public static void showAndWait(AstNode root, String title) {
        showInternal(root, title, true);
    }

    private static void showInternal(AstNode root, String title, boolean waitUntilClosed) {
        Objects.requireNonNull(root, "root cannot be null");
        ensurePlatformReady();

        String windowTitle = (title == null || title.isBlank()) ? "AST Visualizer" : title;

        if (!waitUntilClosed) {
            Platform.runLater(() -> createAndShowWindow(root, windowTitle, null));
            return;
        }

        CountDownLatch closeLatch = new CountDownLatch(1);
        Platform.runLater(() -> createAndShowWindow(root, windowTitle, closeLatch));

        try {
            closeLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void ensurePlatformReady() {
        if (PLATFORM_READY.get()) {
            return;
        }

        synchronized (PLATFORM_LOCK) {
            if (PLATFORM_READY.get()) {
                return;
            }

            CountDownLatch startLatch = new CountDownLatch(1);

            try {
                Platform.startup(() -> {
                    startLatch.countDown();
                });
            } catch (IllegalStateException alreadyStarted) {
                PLATFORM_READY.set(true);
                return;
            } catch (RuntimeException startupError) {
                throw new IllegalStateException(
                        "JavaFX could not start. Make sure a graphical environment and JavaFX runtime are available.",
                        startupError);
            }

            try {
                startLatch.await();
                PLATFORM_READY.set(true);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Interrupted while starting JavaFX platform.", e);
            }
        }
    }

    private static void createAndShowWindow(AstNode root, String title, CountDownLatch closeLatch) {
        TreeView<String> treeView = new TreeView<>(buildTree(root));
        treeView.setShowRoot(true);
        expandAll(treeView.getRoot());
        treeView.setStyle("-fx-font-family: 'SansSerif'; -fx-font-size: 14px;");

        TextArea textView = new TextArea(buildIndentedText(root));
        textView.setEditable(false);
        textView.setWrapText(false);
        textView.setStyle("-fx-font-family: 'Monospaced'; -fx-font-size: 13px;");

        SplitPane splitPane = new SplitPane(treeView, textView);
        splitPane.setOrientation(Orientation.HORIZONTAL);
        splitPane.setDividerPositions(0.55);

        BorderPane rootPane = new BorderPane(splitPane);
        rootPane.setStyle("-fx-background-color: #f8f9fb;");

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(rootPane, 900, 650));

        if (closeLatch != null) {
            stage.setOnHidden(event -> closeLatch.countDown());
        }

        stage.show();
    }

    private static TreeItem<String> buildTree(AstNode node) {
        TreeItem<String> treeItem = new TreeItem<>(node.label());
        for (AstNode child : node.children()) {
            treeItem.getChildren().add(buildTree(child));
        }
        return treeItem;
    }

    private static void expandAll(TreeItem<String> treeItem) {
        if (treeItem == null) {
            return;
        }

        treeItem.setExpanded(true);
        for (TreeItem<String> child : treeItem.getChildren()) {
            expandAll(child);
        }
    }

    private static String buildIndentedText(AstNode node) {
        StringBuilder builder = new StringBuilder();
        appendIndentedText(builder, node, 0);
        return builder.toString();
    }

    private static void appendIndentedText(StringBuilder builder, AstNode node, int depth) {
        builder.append("  ".repeat(Math.max(0, depth)));
        builder.append(node.label());
        builder.append('\n');

        for (AstNode child : node.children()) {
            appendIndentedText(builder, child, depth + 1);
        }
    }
}
