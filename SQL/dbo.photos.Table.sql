USE [easytrip]
GO
/****** Object:  Table [dbo].[photos]    Script Date: 16/04/2025 21.20.22 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[photos](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[attraction_id] [int] NULL,
	[url] [varchar](500) NULL,
	[caption] [nvarchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[photos]  WITH CHECK ADD FOREIGN KEY([attraction_id])
REFERENCES [dbo].[attractions] ([id])
ON DELETE CASCADE
GO
