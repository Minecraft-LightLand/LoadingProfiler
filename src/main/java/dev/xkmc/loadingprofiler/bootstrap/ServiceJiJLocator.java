package dev.xkmc.loadingprofiler.bootstrap;

import com.google.common.collect.ImmutableMap;
import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.ModDirTransformerDiscoverer;
import net.minecraftforge.fml.loading.StringUtils;
import net.minecraftforge.fml.loading.moddiscovery.AbstractJarFileDependencyLocator;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.forgespi.locating.IModLocator;
import net.minecraftforge.forgespi.locating.ModFileLoadingException;
import net.minecraftforge.jarjar.selection.JarSelector;
import org.slf4j.Logger;

import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static cpw.mods.modlauncher.api.LamdbaExceptionUtils.uncheck;

public class ServiceJiJLocator extends AbstractJarFileDependencyLocator implements IModLocator {

	private static final Logger LOGGER = LogUtils.getLogger();
	private static final String MODID = "loadingprofiler";

	private List<IModFile> loadSelf() {
		var excluded = ModDirTransformerDiscoverer.allExcluded();
		var ans = uncheck(() -> Files.list(FMLPaths.MODSDIR.get()))
				.filter(p -> excluded.contains(p) && StringUtils.toLowerCase(p.getFileName().toString()).endsWith(".jar"))
				.sorted(Comparator.comparing(path -> StringUtils.toLowerCase(path.getFileName().toString())));
		return ans.map(this::createMod)
				.filter(e -> e.file() != null && e.file().getModInfos().stream().anyMatch(info -> info.getModId().equals(MODID)))
				.map(IModLocator.ModFileOrException::file).toList();
	}


	@Override
	public String name() {
		return "Loading Profiler JiJ Contents";
	}

	@Override
	public List<ModFileOrException> scanMods() {
		final List<IModFile> sources = loadSelf();
		final List<IModFile> dependenciesToLoad = JarSelector.detectAndSelect(sources, this::loadResourceFromModFile, this::loadModFileFrom, this::identifyMod, this::exception);
		LOGGER.info("Found {} dependencies adding them to mods collection", dependenciesToLoad.size());
		return dependenciesToLoad.stream().map(e -> new ModFileOrException(e, null)).toList();
	}

	@Override
	public List<IModFile> scanMods(final Iterable<IModFile> loadedMods) {
		return List.of();
	}

	private RuntimeException exception(Collection<JarSelector.ResolutionFailureInformation<IModFile>> info) {
		return new IllegalStateException("Failed to load JiJ mods for services");
	}

	@Override
	public void initArguments(final Map<String, ?> arguments) {
		// NO-OP, for now
	}

	@Override
	protected String getDefaultJarModType() {
		return IModFile.Type.GAMELIBRARY.name();
	}

	@SuppressWarnings("resource")
	@Override
	protected Optional<IModFile> loadModFileFrom(final IModFile file, final Path path) {
		try {
			final Path pathInModFile = file.findResource(path.toString());
			final URI filePathUri = new URI("jij:" + (pathInModFile.toAbsolutePath().toUri().getRawSchemeSpecificPart())).normalize();
			final Map<String, ?> outerFsArgs = ImmutableMap.of("packagePath", pathInModFile);
			final FileSystem zipFS = FileSystems.newFileSystem(filePathUri, outerFsArgs);
			final Path pathInFS = zipFS.getPath("/");
			return Optional.of(createMod(pathInFS).file());
		} catch (Exception e) {
			LOGGER.error("Failed to load mod file {} from {}", path, file.getFileName());
			final RuntimeException exception = new ModFileLoadingException("Failed to load mod file " + file.getFileName());
			exception.initCause(e);

			throw exception;
		}
	}

	@Override
	protected String identifyMod(final IModFile modFile) {
		if (modFile.getModFileInfo() == null || modFile.getModInfos().isEmpty()) {
			return modFile.getFileName();
		}
		return modFile.getModInfos().stream().map(IModInfo::getModId).collect(Collectors.joining());
	}

}
